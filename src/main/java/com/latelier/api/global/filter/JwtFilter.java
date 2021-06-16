package com.latelier.api.global.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.latelier.api.domain.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

  public static final String AUTHORIZATION_HEADER = "Authorization";

  private final TokenProvider tokenProvider;


  /**
   * JWT 인증필터
   *
   * @param request  요청
   * @param response 응답
   * @param chain    필터체인
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void doFilter(final ServletRequest request,
                       final ServletResponse response,
                       final FilterChain chain) throws IOException, ServletException {

    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String jwt = resolveToken(httpServletRequest);
    String requestURI = httpServletRequest.getRequestURI();

    if (StringUtils.hasText(jwt)) {
      DecodedJWT decodedJWT = tokenProvider.validateAndDecodeToken(jwt);
      Authentication authentication = tokenProvider.getAuthentication(decodedJWT);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      log.info("Security Context 에 ID '{}' 정보를 저장했습니다. uri : {}", authentication.getName(), requestURI);
    } else {
      log.info("유효한 JWT 토큰이 없습니다. uri: {}", requestURI);
    }

    chain.doFilter(request, response);
  }


  /**
   * 요청헤더에서 JWT 꺼내기
   *
   * @param request 요청
   * @return 토큰이 없다면 null, 있다면 토큰 문자열
   */
  private String resolveToken(final HttpServletRequest request) {

    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }

    return null;
  }

}
