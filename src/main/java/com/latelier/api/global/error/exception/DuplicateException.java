package com.latelier.api.global.error.exception;

public class DuplicateException extends BusinessException {

  public DuplicateException(String value) {
    super(value + "이(가) 중복입니다.", ErrorCode.DUPLICATION);
  }

  public DuplicateException(String value, ErrorCode errorCode) {
    super(value + "이(가) 중복입니다.", errorCode);
  }

}
