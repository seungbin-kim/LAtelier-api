package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;

public class ZoomApiRequestException extends BusinessException {

  public ZoomApiRequestException() {
    super("Zoom API 호출 예외", ErrorCode.ZOOM_API_CALL_FAILED);
  }

}
