package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.ErrorCode;
import com.latelier.api.global.error.exception.NotFoundException;

public class NotFoundEmailException extends NotFoundException {

  public NotFoundEmailException(final String value) {
    super(value, ErrorCode.LOGIN_INPUT_INVALID);
  }

}
