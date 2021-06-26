package com.latelier.api.global.error.exception;

public class NotFoundException extends BusinessException {

  public NotFoundException(String value, ErrorCode errorCode) {
    super(value + "을(를) 찾을 수 없습니다.", errorCode);
  }

}
