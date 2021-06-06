package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.DuplicateException;
import com.latelier.api.global.error.exception.ErrorCode;

public class DuplicateEmailException extends DuplicateException {

  public DuplicateEmailException(final String value) {
    super(value, ErrorCode.DUPLICATE_EMAIL);
  }

}
