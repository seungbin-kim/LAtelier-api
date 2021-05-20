package com.latelier.api.domain.member.packet;

import lombok.Data;

@Data
public class ReqZoomOAuthToken {

  private String access_token;

  private String token_type;

  private String refresh_token;

  private int expires_in;

  private String scope;

}
