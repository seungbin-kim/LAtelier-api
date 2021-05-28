package com.latelier.api.domain.member.packet;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResSms {

  private String requestId;

  private String requestTime;

  private String statusCode;

  private String statusName;

}
