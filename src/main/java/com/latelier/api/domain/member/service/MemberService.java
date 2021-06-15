package com.latelier.api.domain.member.service;

import com.latelier.api.domain.member.entity.Address;
import com.latelier.api.domain.member.entity.Authority;
import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.enumeration.Role;
import com.latelier.api.domain.member.exception.DuplicateEmailAndPhoneNumberException;
import com.latelier.api.domain.member.exception.DuplicateEmailException;
import com.latelier.api.domain.member.exception.DuplicatePhoneNumberException;
import com.latelier.api.domain.member.packet.request.ReqSignUp;
import com.latelier.api.domain.member.packet.response.ResSignUp;
import com.latelier.api.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;

  private final PasswordEncoder passwordEncoder;


  /**
   * 회원 등록하기
   *
   * @param reqSignUp 회원가입 요청정보
   * @return 등록한 회원정보
   */
  @Transactional
  public ResSignUp addMember(final ReqSignUp reqSignUp) {

    Member member = createMember(reqSignUp);
    Member savedMember = memberRepository.save(member);
    return ResSignUp.createSignUpResponse(savedMember);
  }


  /**
   * Member Entity 생성하기
   *
   * @param reqSignUp 회원가입 요청정보
   * @return 생성된 Member Entity
   */
  private Member createMember(final ReqSignUp reqSignUp) {

    checkDuplicateEmailAndPhoneNumber(reqSignUp.getEmail(), reqSignUp.getPhoneNumber());

    Address address = Address.builder()
        .address(reqSignUp.getAddress())
        .zipCode(reqSignUp.getZipCode())
        .build();

    Member member = Member.builder()
        .password(passwordEncoder.encode(reqSignUp.getPassword()))
        .name(reqSignUp.getName())
        .phoneNumber(reqSignUp.getPhoneNumber())
        .email(reqSignUp.getEmail())
        .nickname(reqSignUp.getNickname())
        .introduction(reqSignUp.getIntroduction())
        .address(address)
        .build();

    member
        .getAuthorities()
        .add(new Authority(Role.ROLE_USER));
    return member;
  }


  /**
   * 이메일과 휴대폰번호 중복체크
   *
   * @param email       이메일
   * @param phoneNumber 휴대폰번호
   */
  private void checkDuplicateEmailAndPhoneNumber(final String email, final String phoneNumber) {

    boolean existsEmail = memberRepository.existsByEmail(email);
    boolean existsPhoneNumber = memberRepository.existsByPhoneNumber(phoneNumber);

    if (existsEmail && existsPhoneNumber) {
      throw new DuplicateEmailAndPhoneNumberException(email, phoneNumber);
    } else if (existsEmail) {
      throw new DuplicateEmailException(email);
    } else if (existsPhoneNumber) {
      throw new DuplicatePhoneNumberException(phoneNumber);
    }
  }

}
