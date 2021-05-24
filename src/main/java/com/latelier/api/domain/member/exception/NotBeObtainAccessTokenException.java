package com.latelier.api.domain.member.exception;

public class NotBeObtainAccessTokenException extends RuntimeException {

  public NotBeObtainAccessTokenException() {
    super("Zoom Access Token 획득에 실패하였습니다.");
  }

}
