package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;

public class AccessTokenNotBeObtainedException extends BusinessException {

  public AccessTokenNotBeObtainedException() {
    super("Zoom Access Token 획득에 실패하였습니다.", ErrorCode.ACCESS_TOKEN_NOT_OBTAIN);
  }

}
