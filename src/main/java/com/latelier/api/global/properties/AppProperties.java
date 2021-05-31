package com.latelier.api.global.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("app")
public class AppProperties {

  private final Sms sms;

  @Getter
  @RequiredArgsConstructor
  public static final class Sms {

    private final String redisPrefix;

    private final long expireTime;

  }

}
