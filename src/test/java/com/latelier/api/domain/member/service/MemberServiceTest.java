package com.latelier.api.domain.member.service;

import com.latelier.api.domain.member.exception.EmailAndPhoneNumberDuplicateException;
import com.latelier.api.domain.member.exception.EmailDuplicateException;
import com.latelier.api.domain.member.exception.PhoneNumberDuplicateException;
import com.latelier.api.domain.member.packet.request.ReqSignUp;
import com.latelier.api.domain.member.packet.response.ResSignUp;
import com.latelier.api.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

  @InjectMocks
  MemberService memberService;

  @Mock
  MemberRepository memberRepository;

  @Mock
  PasswordEncoder passwordEncoder;


  @DisplayName("회원등록_성공")
  @ParameterizedTest(name = "[{index}] username={0}, phoneNumber={1}, email={2}, role={4}")
  @CsvSource({
          "홍길동, 01011111111, test1@a.b, !myPassword486@, user",
          "홍길순, 01022222222, test2@a.b, !myPassword486@, instructor",
          "홍길복, 01033333333, test3@a.b, !myPassword486@, admin"})
  void addMemberSuccess(@AggregateWith(SignUpRequestAggregator.class) ReqSignUp req) {
    // given
    given(memberRepository.existsByEmail(req.getEmail()))
        .willReturn(false);
    given(memberRepository.existsByPhoneNumber(req.getPhoneNumber()))
        .willReturn(false);

    // when
    ResSignUp resSignUp = memberService.addMember(req);

    // then
    assertEquals(req.getEmail(), resSignUp.getEmail());
    assertEquals(req.getUsername(), resSignUp.getUsername());
    assertEquals(req.getPhoneNumber(), resSignUp.getPhoneNumber());
    assertEquals(req.getRole(), resSignUp.getRole());
  }


  @DisplayName("회원등록_실패_이메일중복")
  @ParameterizedTest(name = "[{index}] username={0}, phoneNumber={1}, email={2}, role={4}")
  @CsvSource({
          "홍길동, 01011111111, test1@a.b, !myPassword486@, user",
          "홍길순, 01022222222, test2@a.b, !myPassword486@, instructor",
          "홍길복, 01033333333, test3@a.b, !myPassword486@, admin"})
  void addMemberFailEmail(@AggregateWith(SignUpRequestAggregator.class) ReqSignUp req) {
    // given
    given(memberRepository.existsByEmail(req.getEmail()))
        .willReturn(true);
    given(memberRepository.existsByPhoneNumber(req.getPhoneNumber()))
        .willReturn(false);

    // when, then
    assertThrows(
        EmailDuplicateException.class,
        () -> memberService.addMember(req));
  }


  @DisplayName("회원등록_실패_휴대폰중복")
  @ParameterizedTest(name = "[{index}] username={0}, phoneNumber={1}, email={2}, role={4}")
  @CsvSource({
          "홍길동, 01011111111, test1@a.b, !myPassword486@, user",
          "홍길순, 01022222222, test2@a.b, !myPassword486@, instructor",
          "홍길복, 01033333333, test3@a.b, !myPassword486@, admin"})
  void addMemberFailPhoneNumber(@AggregateWith(SignUpRequestAggregator.class) ReqSignUp req) {
    // given
    given(memberRepository.existsByEmail(req.getEmail()))
        .willReturn(false);
    given(memberRepository.existsByPhoneNumber(req.getPhoneNumber()))
        .willReturn(true);

    // when, then
    assertThrows(
        PhoneNumberDuplicateException.class,
        () -> memberService.addMember(req));
  }


  @DisplayName("회원등록_실패_모두중복")
  @ParameterizedTest(name = "[{index}] username={0}, phoneNumber={1}, email={2}, role={4}")
  @CsvSource({
          "홍길동, 01011111111, test1@a.b, !myPassword486@, user",
          "홍길순, 01022222222, test2@a.b, !myPassword486@, instructor",
          "홍길복, 01033333333, test3@a.b, !myPassword486@, admin"})
  void addMemberFail(@AggregateWith(SignUpRequestAggregator.class) ReqSignUp req) {
    // given
    given(memberRepository.existsByEmail(req.getEmail()))
        .willReturn(true);
    given(memberRepository.existsByPhoneNumber(req.getPhoneNumber()))
        .willReturn(true);

    // when, then
    assertThrows(
        EmailAndPhoneNumberDuplicateException.class,
        () -> memberService.addMember(req));
  }


  static class SignUpRequestAggregator implements ArgumentsAggregator {

    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {

      ReqSignUp reqSignUp = new ReqSignUp();
      ReflectionTestUtils.setField(reqSignUp, "username", accessor.getString(0));
      ReflectionTestUtils.setField(reqSignUp, "phoneNumber", accessor.getString(1));
      ReflectionTestUtils.setField(reqSignUp, "email", accessor.getString(2));
      ReflectionTestUtils.setField(reqSignUp, "password", accessor.getString(3));
      ReflectionTestUtils.setField(reqSignUp, "role", accessor.getString(4));
      return reqSignUp;
    }

  }

}