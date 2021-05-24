package com.latelier.api.domain.course.exception;

import com.latelier.api.global.error.exception.NotFoundException;

public class CourseNotFoundException extends NotFoundException {

  public CourseNotFoundException(Long courseId) {
    super("Course ID " + courseId);
  }

}
