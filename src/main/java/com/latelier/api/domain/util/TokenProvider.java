package com.latelier.api.domain.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.latelier.api.global.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenProvider {

  private final JwtProperties jwtProperties;


  /**
   * JWT 생성
   *
   * @param authentication 유저 인증정보
   * @return 유저의 ID, 권한이 담긴 JWT
   */
  public String createToken(final Authentication authentication) {

    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    Date validity =
        new Date(System.currentTimeMillis() + (jwtProperties.getTokenValidityInSeconds() * 1000));

    return JWT.create()
        .withIssuer(jwtProperties.getIssuer())
        .withSubject(authentication.getName())
        .withClaim("roles", authorities)
        .withExpiresAt(validity)
        .sign(Algorithm.HMAC512(jwtProperties.getSecret()));
  }


  /**
   * 토큰에서 인증정보 얻기
   *
   * @param decodedJWT 해독된 토큰
   * @return 인증정보
   */
  public Authentication getAuthentication(final DecodedJWT decodedJWT) {

    Claim roles = decodedJWT.getClaims().get("roles");

    List<SimpleGrantedAuthority> authorities = Arrays.stream(roles.toString().split(","))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    User principal = new User(decodedJWT.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, decodedJWT.getToken(), authorities);
  }


  /**
   * 토큰 확인과 해독
   *
   * @param token Json Web Token
   * @return 해독된 JWT
   */
  public DecodedJWT validateAndDecodeToken(final String token) {

    try {
      JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret()))
          .withIssuer(jwtProperties.getIssuer())
          .build();

      return jwtVerifier.verify(token);
    } catch (Exception e) {
      throw new InvalidTokenException();
    }
  }

}
