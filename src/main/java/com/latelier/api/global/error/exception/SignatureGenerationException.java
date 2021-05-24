package com.latelier.api.global.error.exception;

public class SignatureGenerationException extends RuntimeException {

  public SignatureGenerationException() {
    super("서명 생성에 실패하였습니다.");
  }

}
