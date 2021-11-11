package com.latelier.api.domain.member.service;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.packet.response.ResCourseSimple;
import com.latelier.api.domain.member.entity.*;
import com.latelier.api.domain.member.enumeration.OrderState;
import com.latelier.api.domain.member.exception.MemberNotFoundException;
import com.latelier.api.domain.member.packet.response.ResOrder;
import com.latelier.api.domain.member.repository.*;
import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final IamportClient iamportClient;

    private final MemberRepository memberRepository;

    private final CartRepository cartRepository;

    private final OrderCourseRepository orderCourseRepository;

    private final OrderRepository orderRepository;

    private final EnrollmentRepository enrollmentRepository;


    /**
     * 실제 결제금액을 검증하고, 구매 처리 프로세스를 진행
     * 구매내역 DB 등록, 회원 강의등록
     *
     * @param currentMemberId 현재 로그인 한 유저의 ID
     * @param orderMemberId   결제한 유저의 ID
     * @param impUid          Iamport 결제 ID
     * @param isTest          테스트 여부(관리자)
     */
    @Transactional(rollbackFor = Exception.class)
    public ResOrder verifyAndProcess(final Long currentMemberId,
                                     final Long orderMemberId,
                                     final String impUid,
                                     final boolean isTest) throws IamportResponseException, IOException {

        if (!currentMemberId.equals(orderMemberId)) {
            if (!isTest) iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));
            throw new BusinessException(ErrorCode.MEMBER_NOT_MATCH);
        }

        Member member = getMemberById(currentMemberId);
        Map<Boolean, List<Course>> toBeDeletedAndToBeOrdered =
                cartRepository.findAllWithCourseByMember(member).stream()
                        .map(Cart::getCourse)
                        .collect(Collectors.partitioningBy(course -> course.isFull() || course.hasEnded()));
        List<Course> toBeDeletedCoursesAtCart = toBeDeletedAndToBeOrdered.get(true);

        if (toBeDeletedCoursesAtCart.isEmpty()) { // 기간이 지났거나 인원이 초과되는 강의가 없다면 정상진행
            List<Course> courses = toBeDeletedAndToBeOrdered.get(false);
            Payment payment = isTest ? null : iamportClient.paymentByImpUid(impUid).getResponse(); // 결제정보 얻어오기
            int paidAmount = isTest ? 0 : payment.getAmount().intValue();
            String orderName = isTest ? "테스트결제" : payment.getName();
            if (!isSameAmount(paidAmount, courses, isTest)) {
                iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));
                throw new BusinessException(ErrorCode.PAYMENT_FORGERY);
            }
            return orderProcess(impUid, member, courses, orderName, paidAmount);
        } else { // 기간이 지났거나 인원이 초과된다면 결제를 취소하고 해당되는 항목들을 장바구니에서 삭제
            if (!isTest) iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));
            cartRepository.deleteByCourseIn(toBeDeletedCoursesAtCart);
            return null;
        }
    }


    /**
     * 구매처리
     *
     * @param impUid     iamport 결제 ID
     * @param member     사용자
     * @param courses    구매 강의목록
     * @param orderName  주문 이름
     * @param paidAmount 지불 금액
     * @return 주문처리 결과
     */
    private ResOrder orderProcess(final String impUid,
                                  final Member member,
                                  final List<Course> courses,
                                  final String orderName,
                                  final int paidAmount) {

        Order order = Order.of(impUid, member, orderName, paidAmount, OrderState.PAID);
        orderRepository.save(order);
        saveOrderCourses(courses, order);
        enrollCourses(member, courses);
        cartRepository.deleteAllByMember(member);
        return ResOrder.of(order.getId(),
                paidAmount, order.getCreatedAt(),
                courses.stream()
                        .map(c -> ResCourseSimple.of(c, null))
                        .collect(Collectors.toList()));
    }


    /**
     * 강의를 회원에게 등록
     *
     * @param member  사용자
     * @param courses 강의 리스트
     */
    private void enrollCourses(final Member member,
                               final List<Course> courses) {

        enrollmentRepository.saveAll(courses.stream()
                .map(course -> {
                    return Enrollment.of(member, course);
                })
                .collect(Collectors.toList()));
    }


    /**
     * 강의 주문내역을 만들고 저장
     *
     * @param courses 강의 리스트
     * @param order   주문 Entity
     */
    private void saveOrderCourses(final List<Course> courses,
                                  final Order order) {

        orderCourseRepository.saveAll(courses.stream()
                .map(course -> OrderCourse.of(order, course, 1, course.getPrice()))
                .collect(Collectors.toList()));
    }


    /**
     * 결제금액 검증
     *
     * @param paidAmount 결제된 금액
     * @param courses    강의 리스트
     * @return 검증
     */
    private boolean isSameAmount(final int paidAmount,
                                 final List<Course> courses,
                                 final boolean isTest) {

        if (isTest) return true;
        int amountToBePaid = courses.stream()
                .mapToInt(Course::getPrice)
                .sum();
        return paidAmount == amountToBePaid;
    }


    /**
     * 회원 ID로 회원찾기
     * 찾지 못할시 예외발생
     *
     * @param memberId 회원 ID
     * @return ID 에 해당하는 회원
     */
    public Member getMemberById(final Long memberId) {

        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(String.valueOf(memberId)));
    }

}
