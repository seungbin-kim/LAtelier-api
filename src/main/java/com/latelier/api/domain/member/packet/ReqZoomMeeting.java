package com.latelier.api.domain.member.packet;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ReqZoomMeeting {

  private String topic;

  private int type;

  private String timezone;

  public ReqZoomMeeting(final String topic) {
    this.topic = topic;
    this.type = 1;
    this.timezone = "Asia/Seoul";
  }

}
