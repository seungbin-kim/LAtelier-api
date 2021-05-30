package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;

public class NotBeObtainMeetingInformationException extends BusinessException {

  public NotBeObtainMeetingInformationException() {
    super("회의 정보를 얻지 못하였습니다.", ErrorCode.NOT_OBTAIN_MEETING_INFORMATION);
  }

}
