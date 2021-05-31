package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;

public class ZoomAccessTokenRequestException extends BusinessException {

  public ZoomAccessTokenRequestException() {
    super("Zoom Access Token 요청에 실패하였습니다.", ErrorCode.ACCESS_TOKEN_REQUEST_FAILED);
  }

}
