package com.latelier.api.domain.member.service;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.exception.CourseNotFoundException;
import com.latelier.api.domain.course.repository.CourseRepository;
import com.latelier.api.domain.member.entity.Cart;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.exception.EmailAndPhoneNumberDuplicateException;
import com.latelier.api.domain.member.exception.EmailDuplicateException;
import com.latelier.api.domain.member.exception.MemberNotFoundException;
import com.latelier.api.domain.member.exception.PhoneNumberDuplicateException;
import com.latelier.api.domain.member.packet.request.ReqSignUp;
import com.latelier.api.domain.member.repository.CartRepository;
import com.latelier.api.domain.member.repository.MemberRepository;
import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final CourseRepository courseRepository;

    private final CartRepository cartRepository;

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
    public void addInUserCart(final Long memberId,
                              final Long courseId) {

        Member member = getMemberById(memberId);
        Course course = getCourseById(courseId);
        cartRepository.save(Cart.of(member, course));
    }


    private Course getCourseById(final Long courseId) {

        return courseRepository.findById(courseId)
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