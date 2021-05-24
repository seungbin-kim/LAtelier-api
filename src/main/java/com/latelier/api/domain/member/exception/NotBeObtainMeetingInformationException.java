package com.latelier.api.domain.member.exception;

public class NotBeObtainMeetingInformationException extends RuntimeException {

  public NotBeObtainMeetingInformationException() {
    super("회의 정보를 얻지 못하였습니다.");
  }

}
