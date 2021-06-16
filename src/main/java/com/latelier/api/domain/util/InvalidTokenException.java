package com.latelier.api.domain.util;

import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;

public class InvalidTokenException extends BusinessException {

  public InvalidTokenException() {
    super("유효하지 않은 토큰", ErrorCode.INVALID_TOKEN);
  }

}
