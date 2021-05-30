package com.latelier.api.domain.util;

import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;

public class SignatureGenerationException extends BusinessException {

  public SignatureGenerationException() {
    super("Signature 생성 실패", ErrorCode.INTERNAL_SERVER_ERROR);
  }

}
