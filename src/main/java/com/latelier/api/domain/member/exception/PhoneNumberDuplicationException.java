package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.DuplicationException;
import com.latelier.api.global.error.exception.ErrorCode;

public class PhoneNumberDuplicationException extends DuplicationException {

  public PhoneNumberDuplicationException(String value) {
    super(value, ErrorCode.PHONE_NUMBER_DUPLICATION);
  }

}
