package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.DuplicateException;
import com.latelier.api.global.error.exception.ErrorCode;

public class DuplicateEmailAndPhoneNumberException extends DuplicateException {

  public DuplicateEmailAndPhoneNumberException(final String email, final String phoneNumber) {
    super("email: " + email + ", phoneNumber: " + phoneNumber, ErrorCode.DUPLICATE_EMAIL_AND_PHONE_NUMBER);
  }

}
