package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;

public class MemberNotActivatedException extends BusinessException {

  public MemberNotActivatedException(final String email) {

    super(email + " 계정은 활성화되어 있지 않습니다.", ErrorCode.NOT_ACTIVATED);
  }

}
