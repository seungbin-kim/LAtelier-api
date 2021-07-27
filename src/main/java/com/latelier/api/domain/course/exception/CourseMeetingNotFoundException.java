package com.latelier.api.domain.course.exception;

import com.latelier.api.global.error.exception.ErrorCode;
import com.latelier.api.global.error.exception.NotFoundException;

public class CourseMeetingNotFoundException extends NotFoundException {

  public CourseMeetingNotFoundException(final Long courseId) {

    super("Course ID " + courseId + " 에 대한 미팅정보", ErrorCode.COURSE_MEETING_NOT_FOUND);
  }

}
