package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.DuplicateException;
import com.latelier.api.global.error.exception.ErrorCode;

public class DuplicatePhoneNumberException extends DuplicateException {

  public DuplicatePhoneNumberException(final String value) {
    super(value, ErrorCode.DUPLICATE_PHONE_NUMBER);
  }

}
