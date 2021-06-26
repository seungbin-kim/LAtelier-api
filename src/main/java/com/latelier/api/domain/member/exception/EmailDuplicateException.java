package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.DuplicateException;
import com.latelier.api.global.error.exception.ErrorCode;

public class EmailDuplicateException extends DuplicateException {

  public EmailDuplicateException(final String value) {
    super(value, ErrorCode.DUPLICATE_EMAIL);
  }

}
