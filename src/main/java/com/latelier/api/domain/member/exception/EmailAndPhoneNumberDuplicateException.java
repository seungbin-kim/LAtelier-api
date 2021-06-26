package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.DuplicateException;
import com.latelier.api.global.error.exception.ErrorCode;

public class EmailAndPhoneNumberDuplicateException extends DuplicateException {

  public EmailAndPhoneNumberDuplicateException(final String email, final String phoneNumber) {
    super("email: " + email + ", phoneNumber: " + phoneNumber, ErrorCode.EMAIL_AND_PHONE_NUMBER_DUPLICATE);
  }

}
