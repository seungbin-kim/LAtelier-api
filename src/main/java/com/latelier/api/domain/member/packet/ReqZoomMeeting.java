package com.latelier.api.domain.member.packet;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReqZoomMeeting {

  private final String topic;

  private final int type;

  private final String timezone;


  public static ReqZoomMeeting createReqZoomMeeting(final String topic) {

    return new ReqZoomMeeting(topic, 1, "Asia/Seoul");
  }

}
