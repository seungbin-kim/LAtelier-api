package com.latelier.api.domain.member.service;

import com.latelier.api.domain.member.packet.request.ReqSignIn;
import com.latelier.api.domain.util.TokenProvider;
import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAuthService {

  private final TokenProvider tokenProvider;

  private final AuthenticationManagerBuilder authenticationManagerBuilder;


  /**
   * 로그인
   *
   * @param reqSignIn 로그인 요청정보
   * @return Json Web Token
   */
  @Transactional
  public String signIn(final ReqSignIn reqSignIn) {

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(reqSignIn.getEmail(), reqSignIn.getPassword());

    try {
      Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

      return tokenProvider.createToken(authentication);
    } catch (Exception e) {
      throw new BusinessException(e.getMessage(), ErrorCode.LOGIN_INPUT_INVALID);
    }
  }

}
