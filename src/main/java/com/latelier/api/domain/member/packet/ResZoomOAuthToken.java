package com.latelier.api.domain.member.packet;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ResZoomOAuthToken {

  private String access_token;

  private String token_type;

  private String refresh_token;

  private int expires_in;

  private String scope;

}
