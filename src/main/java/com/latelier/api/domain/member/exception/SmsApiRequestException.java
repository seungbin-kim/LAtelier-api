package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;

public class SmsApiRequestException extends BusinessException {

  public SmsApiRequestException() {

    super("Naver SENS API 호출 예외", ErrorCode.SMS_API_CALL_FAILED);
  }

}
