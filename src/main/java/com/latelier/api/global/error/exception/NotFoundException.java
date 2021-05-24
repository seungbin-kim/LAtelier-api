package com.latelier.api.global.error.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message + "을(를) 찾을 수 없습니다.");
  }

}
