package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.DuplicateException;
import com.latelier.api.global.error.exception.ErrorCode;

public class PhoneNumberDuplicateException extends DuplicateException {

  public PhoneNumberDuplicateException(final String value) {
    super(value, ErrorCode.DUPLICATE_PHONE_NUMBER);
  }

}
