package com.latelier.api.domain.member.packet;

import lombok.Data;

@Data
public class ReqZoomMeeting {

  private String topic;

  private int type;

  private String timezone;

  public ReqZoomMeeting(String topic) {
    this.topic = topic;
    this.type = 1;
    this.timezone = "Asia/Seoul";
  }

}
