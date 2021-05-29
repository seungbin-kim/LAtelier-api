package com.latelier.api.global.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("naver")
public class NaverProperties {

  private final CloudPlatform cloudPlatform;

  @Getter
  @RequiredArgsConstructor
  public static final class CloudPlatform {

    private final String key;

    private final String secret;

    private final Sens sens;

    @Getter
    @RequiredArgsConstructor
    public static final class Sens {

      private final String smsFrom;

      private final String url;

      private final String serviceId;

    }

  }

}
