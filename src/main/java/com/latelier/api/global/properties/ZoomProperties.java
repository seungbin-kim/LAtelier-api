package com.latelier.api.global.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("zoom")
public final class ZoomProperties {

  private final Url url;

  private final OauthApp oauthApp;

  private final Api api;

  @Getter
  @RequiredArgsConstructor
  public static final class Url {

    private final String redirect;

    private final String oauth;

    private final String meetingCreation;

  }

  @Getter
  @RequiredArgsConstructor
  public static final class OauthApp {

    private final String dev;

    private final String prod;

  }

  @Getter
  @RequiredArgsConstructor
  public static final class Api {

    private final String key;

    private final String secret;

  }

}
