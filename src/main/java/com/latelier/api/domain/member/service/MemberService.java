package com.latelier.api.domain.member.service;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.enumuration.CourseState;
import com.latelier.api.domain.course.exception.CourseNotFoundException;
import com.latelier.api.domain.course.repository.CourseRepository;
import com.latelier.api.domain.file.entity.CourseFile;
import com.latelier.api.domain.file.entity.File;
import com.latelier.api.domain.file.enumuration.FileGroup;
import com.latelier.api.domain.file.repository.CourseFileRepository;
import com.latelier.api.domain.member.entity.Cart;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.exception.EmailAndPhoneNumberDuplicateException;
import com.latelier.api.domain.member.exception.EmailDuplicateException;
import com.latelier.api.domain.member.exception.MemberNotFoundException;
import com.latelier.api.domain.member.exception.PhoneNumberDuplicateException;
import com.latelier.api.domain.member.packet.request.ReqSignUp;
import com.latelier.api.domain.member.packet.response.ResAddCart;
import com.latelier.api.domain.member.packet.response.ResMyCart;
import com.latelier.api.domain.member.repository.CartRepository;
import com.latelier.api.domain.member.repository.EnrollmentRepository;
import com.latelier.api.domain.member.repository.MemberRepository;
import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final CourseRepository courseRepository;

    private final CourseFileRepository courseFileRepository;

    private final CartRepository cartRepository;

    private final EnrollmentRepository enrollmentRepository;

    private final PasswordEncoder passwordEncoder;


    /**
     * 로그인 이메일과 비밀번호로 사용자 확인하기
     *
     * @param email    사용자 이메일
     * @param password 사용자 비밀번호
     * @return 사용자 정보
     */
    public Member authenticate(final String email,
                               final String password) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_INPUT_INVALID));
        checkPassword(member, password);
        return member;
    }


    /**
     * 패스워드 확인
     *
     * @param member   사용자
     * @param password 입력 비밀번호
     */
    private void checkPassword(final Member member,
                               final String password) {

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_INPUT_INVALID);
        }
    }


    /**
     * 회원 등록하기
     *
     * @param reqSignUp 회원등록 요청정보
     * @return 등록한 회원
     */
    @Transactional
    public Member addMember(final ReqSignUp reqSignUp) {

        checkDuplicateEmailAndPhoneNumber(reqSignUp.getEmail(), reqSignUp.getPhoneNumber());
        Member member = createMember(reqSignUp);
        return memberRepository.save(member);
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


    /**
     * 사용자의 장바구니 목록에 강의를 추가
     *
     * @param memberId 사용자 ID
     * @param courseId 강의 ID
     */
    @Transactional
    public ResAddCart addInUserCart(final Long memberId,
                                    final Long courseId) {

        Member member = getMemberById(memberId);
        Course course = getOpenedCourseById(courseId);
        checkCanAdd(member, course);
        Cart cart = cartRepository.save(Cart.of(member, course));
        return ResAddCart.of(cart);
    }


    /**
     * 사용자의 장바구니 목록 조회
     *
     * @param memberId 사용자 ID
     * @return 사용자 장바구니
     */
    public ResMyCart getUserCartList(final Long memberId) {

        Member member = getMemberById(memberId);

        // 사용자의 장바구니 목록 얻어와 강의들을 추출
        List<Cart> cartList = cartRepository.findAllWithCourseByMember(member);
        List<Course> courseList = cartList.stream()
                .map(Cart::getCourse)
                .collect(Collectors.toList());

        // 장바구니 목록 강의들의 썸네일 이미지 추출
        List<CourseFile> courseFileList = courseFileRepository
                .findWithFileByFileGroupAndCourses(FileGroup.COURSE_THUMBNAIL_IMAGE, courseList);
        Map<Course, File> courseFileMap = mappingCourseThumbnail(courseFileList);

        // 강의와 강의 썸네일 이미지 주소 결합, 장바구니에 담긴 전체요소의 가격 구하기
        List<ResMyCart.CartElement> cartElements = cartList.stream()
                .map(cart -> ResMyCart.CartElement.of(cart, courseFileMap.get(cart.getCourse())))
                .collect(Collectors.toList());
        int totalPrice = cartElements.stream()
                .mapToInt(ResMyCart.CartElement::getCoursePrice)
                .sum();
        return ResMyCart.of(cartElements, totalPrice, member);
    }


    /**
     * 장바구니에서 특정 요소를 제거
     *
     * @param memberId 사용자 ID
     * @param cartId   장바구니 요소 ID
     */
    @Transactional
    public void deleteInUserCart(final Long memberId, final Long cartId) {

        Member member = getMemberById(memberId);
        if (cartRepository.deleteByIdAndMember(cartId, member) == 0) {
            throw new BusinessException(ErrorCode.CART_NOT_FOUND);
        }
    }


    /**
     * 사용자 장바구니 모두 비우기
     *
     * @param memberId 사용자 ID
     */
    @Transactional
    public void deleteAllInUserCart(final Long memberId) {

        Member member = getMemberById(memberId);
        cartRepository.deleteAllByMember(member);
    }


    /**
     * 이미 구매하였거나 장바구니에 있는 강의인지 확인합니다.
     *
     * @param member 사용자
     * @param course 강의
     */
    private void checkCanAdd(final Member member,
                             final Course course) {

        if (cartRepository.existsByMemberAndCourse(member, course)) {
            throw new BusinessException(ErrorCode.CART_DUPLICATE);
        } else if (enrollmentRepository.existsByMemberIdAndCourseId(member.getId(), course.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_ENROLLED);
        }
    }


    /**
     * 강의와 강의 썸네일 이미지를 매핑
     *
     * @param courseFiles 강의파일 리스트
     * @return 매핑된 Map
     */
    private Map<Course, File> mappingCourseThumbnail(final List<CourseFile> courseFiles) {

        Map<Course, File> courseImageFileMap = new HashMap<>();
        courseFiles.forEach(courseFile -> courseImageFileMap.put(courseFile.getCourse(), courseFile.getFile()));
        return courseImageFileMap;
    }


    /**
     * 열린 강의 얻어오기
     *
     * @param courseId 강의 ID
     * @return 강의 Entity
     */
    private Course getOpenedCourseById(final Long courseId) {

        return courseRepository.findByIdAndStateLike(courseId, CourseState.APPROVED)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }


    /**
     * Member Entity 생성하기
     *
     * @param reqSignUp 등록 요청정보
     * @return 생성된 Member Entity
     */
    private Member createMember(final ReqSignUp reqSignUp) {

        return Member.of(
                reqSignUp.getEmail(),
                reqSignUp.getPhoneNumber(),
                reqSignUp.getUsername(),
                passwordEncoder.encode(reqSignUp.getPassword()),
                reqSignUp.getRole());
    }


    /**
     * 이메일과 휴대폰번호 중복체크
     * 중복시 예외발생
     *
     * @param email       이메일
     * @param phoneNumber 휴대폰번호
     */
    private void checkDuplicateEmailAndPhoneNumber(final String email,
                                                   final String phoneNumber) {

        boolean existsEmail = memberRepository.existsByEmail(email);
        boolean existsPhoneNumber = memberRepository.existsByPhoneNumber(phoneNumber);

        if (existsEmail && existsPhoneNumber) {
            throw new EmailAndPhoneNumberDuplicateException(email, phoneNumber);
        } else if (existsEmail) {
            throw new EmailDuplicateException(email);
        } else if (existsPhoneNumber) {
            throw new PhoneNumberDuplicateException(phoneNumber);
        }
    }
}