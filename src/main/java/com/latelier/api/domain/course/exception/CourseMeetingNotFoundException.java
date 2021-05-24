package com.latelier.api.domain.course.exception;

import com.latelier.api.global.error.exception.NotFoundException;

public class CourseMeetingNotFoundException extends NotFoundException {

  public CourseMeetingNotFoundException(final Long courseId) {
    super("Course ID " + courseId + " 에 대한 미팅정보");
  }

}
