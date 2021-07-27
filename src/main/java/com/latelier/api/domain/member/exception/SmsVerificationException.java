package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;

public class SmsVerificationException extends BusinessException {

  public SmsVerificationException() {

    super("인증번호 확인 실패", ErrorCode.SMS_VERIFICATION_FAILED);
  }

}
