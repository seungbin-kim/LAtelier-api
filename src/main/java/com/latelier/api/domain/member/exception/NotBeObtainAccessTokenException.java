package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;

public class NotBeObtainAccessTokenException extends BusinessException {

  public NotBeObtainAccessTokenException() {
    super("Zoom Access Token 획득에 실패하였습니다.", ErrorCode.NOT_OBTAIN_ACCESS_TOKEN);
  }

}
