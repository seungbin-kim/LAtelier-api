package com.latelier.api.domain.member.service;

import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.exception.EmailAndPhoneNumberDuplicateException;
import com.latelier.api.domain.member.exception.EmailDuplicateException;
import com.latelier.api.domain.member.exception.PhoneNumberDuplicateException;
import com.latelier.api.domain.member.packet.request.ReqSignUp;
import com.latelier.api.domain.member.packet.response.ResSignUp;
import com.latelier.api.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

  @InjectMocks
  MemberService memberService;

  @Mock
  MemberRepository memberRepository;

  @Mock
  PasswordEncoder passwordEncoder;


  @Test
  @DisplayName("회원등록_성공")
  void addMemberSuccess() {
    // given
    ReqSignUp reqSignUp = new ReqSignUp();
    String name = "홍길동";
    String phoneNumber = "01012345678";
    String email = "test@a.b";
    String password = "myPassword";
    ReflectionTestUtils.setField(reqSignUp, "name", name);
    ReflectionTestUtils.setField(reqSignUp, "phoneNumber", phoneNumber);
    ReflectionTestUtils.setField(reqSignUp, "email", email);
    ReflectionTestUtils.setField(reqSignUp, "password", password);

    Member member = Member.builder()
        .email(email)
        .name(name)
        .phoneNumber(phoneNumber)
        .password(password)
        .build();

    given(memberRepository.save(any()))
        .willReturn(member);
    given(memberRepository.existsByEmail(email))
        .willReturn(false);
    given(memberRepository.existsByPhoneNumber(phoneNumber))
        .willReturn(false);

    // when
    ResSignUp resSignUp = memberService.addMember(reqSignUp);

    // then
    assertEquals(email, resSignUp.getEmail());
    assertEquals(name, resSignUp.getName());
    assertEquals(phoneNumber, resSignUp.getPhoneNumber());
  }


  @Test
  @DisplayName("회원등록_실패_이메일중복")
  void addMemberFailEmail() {
    // given
    ReqSignUp reqSignUp = new ReqSignUp();
    String name = "홍길동";
    String phoneNumber = "01012345678";
    String email = "test@a.b";
    String password = "myPassword";
    ReflectionTestUtils.setField(reqSignUp, "name", name);
    ReflectionTestUtils.setField(reqSignUp, "phoneNumber", phoneNumber);
    ReflectionTestUtils.setField(reqSignUp, "email", email);
    ReflectionTestUtils.setField(reqSignUp, "password", password);

    Member member = Member.builder()
        .email(email)
        .name(name)
        .phoneNumber(phoneNumber)
        .password(password)
        .build();

    given(memberRepository.existsByEmail(email))
        .willReturn(true);
    given(memberRepository.existsByPhoneNumber(phoneNumber))
        .willReturn(false);

    // when, then
    assertThrows(
        EmailDuplicateException.class,
        () -> memberService.addMember(reqSignUp));
  }


  @Test
  @DisplayName("회원등록_실패_휴대폰중복")
  void addMemberFailPhoneNumber() {
    // given
    ReqSignUp reqSignUp = new ReqSignUp();
    String name = "홍길동";
    String phoneNumber = "01012345678";
    String email = "test@a.b";
    String password = "myPassword";
    ReflectionTestUtils.setField(reqSignUp, "name", name);
    ReflectionTestUtils.setField(reqSignUp, "phoneNumber", phoneNumber);
    ReflectionTestUtils.setField(reqSignUp, "email", email);
    ReflectionTestUtils.setField(reqSignUp, "password", password);

    Member member = Member.builder()
        .email(email)
        .name(name)
        .phoneNumber(phoneNumber)
        .password(password)
        .build();

    given(memberRepository.existsByEmail(email))
        .willReturn(false);
    given(memberRepository.existsByPhoneNumber(phoneNumber))
        .willReturn(true);

    // when, then
    assertThrows(
        PhoneNumberDuplicateException.class,
        () -> memberService.addMember(reqSignUp));
  }


  @Test
  @DisplayName("회원등록_실패_모두중복")
  void addMemberFail() {
    // given
    ReqSignUp reqSignUp = new ReqSignUp();
    String name = "홍길동";
    String phoneNumber = "01012345678";
    String email = "test@a.b";
    String password = "myPassword";
    ReflectionTestUtils.setField(reqSignUp, "name", name);
    ReflectionTestUtils.setField(reqSignUp, "phoneNumber", phoneNumber);
    ReflectionTestUtils.setField(reqSignUp, "email", email);
    ReflectionTestUtils.setField(reqSignUp, "password", password);

    Member member = Member.builder()
        .email(email)
        .name(name)
        .phoneNumber(phoneNumber)
        .password(password)
        .build();

    given(memberRepository.existsByEmail(email))
        .willReturn(true);
    given(memberRepository.existsByPhoneNumber(phoneNumber))
        .willReturn(true);

    // when, then
    assertThrows(
        EmailAndPhoneNumberDuplicateException.class,
        () -> memberService.addMember(reqSignUp));
  }

}