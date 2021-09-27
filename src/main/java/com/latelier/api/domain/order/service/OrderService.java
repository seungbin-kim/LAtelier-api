package com.latelier.api.domain.order.service;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.member.entity.Cart;
import com.latelier.api.domain.member.entity.Enrollment;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.exception.MemberNotFoundException;
import com.latelier.api.domain.member.repository.CartRepository;
import com.latelier.api.domain.member.repository.EnrollmentRepository;
import com.latelier.api.domain.member.repository.MemberRepository;
import com.latelier.api.domain.order.entity.Order;
import com.latelier.api.domain.order.entity.OrderCourse;
import com.latelier.api.domain.order.enumeration.OrderState;
import com.latelier.api.domain.order.repository.OrderCourseRepository;
import com.latelier.api.domain.order.repository.OrderRepository;
import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
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
     * @param impUid          Iamport 결제 고유 ID
     * @throws IamportResponseException
     * @throws IOException
     */
    @Transactional
    public void verifyAndProcess(final Long currentMemberId,
                                 final Long orderMemberId,
                                 final String impUid,
                                 final boolean isTest) throws IamportResponseException, IOException {

        if (!currentMemberId.equals(orderMemberId)) throw new BusinessException(ErrorCode.MEMBER_NOT_MATCH);

        Member member = getMemberById(currentMemberId);
        List<Course> courses = cartRepository.findAllWithCourseByMember(member).stream()
                .map(Cart::getCourse)
                .collect(Collectors.toList());

        int paid = verifyAmount(impUid, courses, isTest);
        orderProcess(impUid, member, courses, paid);
    }


    /**
     * 구매처리 과정을 진행
     *
     * @param impUid  결제 고유 ID
     * @param member  사용자
     * @param courses 강의목록
     * @param paid    지불 금액
     */
    private void orderProcess(final String impUid,
                              final Member member,
                              final List<Course> courses,
                              final int paid) {

        Order order = Order.of(impUid, member, paid, OrderState.PAID);
        orderRepository.save(order);
        saveOrderCourses(courses, order);
        enrollCourses(member, courses);
        cartRepository.deleteAllByMember(member);
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
                .map(course -> Enrollment.of(member, course))
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
                .map(course -> OrderCourse.of(order, course, 1, course.getCoursePrice()))
                .collect(Collectors.toList()));
    }


    /**
     * 결제금액 검증
     *
     * @param impUid  Iamport 결제 고유 ID
     * @param courses 강의 리스트
     * @return 결제된 금액
     * @throws IamportResponseException
     * @throws IOException
     */
    private int verifyAmount(final String impUid,
                             final List<Course> courses,
                             final boolean isTest) throws IamportResponseException, IOException {

        int amount = isTest ? 0 : iamportClient.paymentByImpUid(impUid).getResponse().getAmount().intValue();
        int amountToBePaid = courses.stream()
                .mapToInt(Course::getCoursePrice)
                .sum();
        if (!isTest && amount != amountToBePaid) {
            throw new BusinessException(ErrorCode.PAYMENT_FORGERY);
        }
        return amountToBePaid;
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
