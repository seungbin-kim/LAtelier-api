package com.latelier.api.domain.member.service;

import com.latelier.api.domain.member.entity.Member;
import com.latelier.api.domain.member.exception.NotActivatedMemberException;
import com.latelier.api.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;


  /**
   * 로그인시 동작
   *
   * @param email 유저의 이메일
   * @return UserDetails
   */
  @Override
  public UserDetails loadUserByUsername(final String email) {

    return memberRepository.findByEmailWithAuthorities(email)
        .map(this::createUserDetails)
        .orElseThrow(() -> new InternalAuthenticationServiceException("찾을 수 없는 사용자." + email));
  }


  /**
   * UserDetails 생성
   *
   * @param member Member 엔티티
   * @return UserDetails
   */
  private User createUserDetails(final Member member) {

    if (!member.isActivated()) {
      throw new NotActivatedMemberException(member.getEmail());
    }

    List<SimpleGrantedAuthority> grantedAuthorities = member.getAuthorities().stream()
        .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName().toString()))
        .collect(Collectors.toList());

    return new User(String.valueOf(member.getId()), member.getPassword(), grantedAuthorities);
  }

}
