package com.latelier.api.domain.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.latelier.api.domain.member.enumeration.Role;
import com.latelier.api.global.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Collections;
import java.util.List;

@Slf4j
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

        String authority = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseGet(Role.ROLE_USER::toString);

        Date validity =
                new Date(System.currentTimeMillis() + (jwtProperties.getTokenValidityInSeconds() * 1000));

        return JWT.create()
                .withIssuer(jwtProperties.getIssuer())
                .withSubject(authentication.getName())
                .withClaim("role", authority)
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

        Claim roles = decodedJWT.getClaims().get("role");

        List<SimpleGrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(roles.asString()));

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
            log.error("토큰해독 실패", e);
        }
        return null;
    }


    /**
     * 토큰으로 쿠키 생성
     * @param token JWT
     * @return 응답 쿠키
     */
    public ResponseCookie createTokenCookie(final String token) {

        long ageInSeconds = StringUtils.hasText(token) ? jwtProperties.getTokenValidityInSeconds() : 0;
        return ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(ageInSeconds)
                .build();
    }


    /**
     * 재발급 기준 확인
     *
     * @param decodedJWT 해독된 JWT
     * @return true - 재발급 필요, false - 재발급 불필요
     */
    public boolean checkTokenReissue(final DecodedJWT decodedJWT) {

        return decodedJWT.getExpiresAt()
                .before(new Date(System.currentTimeMillis() + jwtProperties.getTokenReissueCriteriaInSeconds()));
    }

}
