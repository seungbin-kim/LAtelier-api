package com.latelier.api.global.error.exception;

public class DuplicationException extends BusinessException {

  public DuplicationException(String value) {
    super(value + "이(가) 중복입니다.", ErrorCode.DUPLICATION);
  }

  public DuplicationException(String value, ErrorCode errorCode) {
    super(value + "이(가) 중복입니다.", errorCode);
  }

}
