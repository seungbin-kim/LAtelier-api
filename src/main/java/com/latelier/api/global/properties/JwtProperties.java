package com.latelier.api.global.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("jwt")
public class JwtProperties {

  private final String issuer;

  private final String secret;

  private final Long tokenValidityInSeconds;

}
