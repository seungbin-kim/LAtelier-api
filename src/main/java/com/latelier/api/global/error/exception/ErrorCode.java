package com.latelier.api.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // Common
  METHOD_NOT_ALLOWED(405, "C001", "Method Not Allowed"),
  INVALID_INPUT_VALUE(400, "C002", "Invalid Input Value"),
  INVALID_TOKEN(401, "C003", "Invalid Token"),
  INSUFFICIENT_SCOPE(403, "C004", "Insufficient Scope"),
  NOT_FOUND(404, "C005", "Not Found"),
  DUPLICATION(409, "C006", "Duplication"),
  INTERNAL_SERVER_ERROR(500, "C007", "Server Error"),

  // Member
  LOGIN_INPUT_INVALID(400, "M001", "Login input is invalid"),
  EMAIL_DUPLICATION(409, "M002", "Email is Duplication"),
  PHONE_NUMBER_DUPLICATION(409, "M003", "Phone Number is Duplication"),

  NOT_OBTAIN_ACCESS_TOKEN(500, "M010", "Not Obtain Access Token"),
  NOT_OBTAIN_MEETING_INFORMATION(500, "M011", "Not Obtain Meeting Information"),

  // Course
  COURSE_NOT_FOUND(404, "CS001", "Course Not Found"),
  COURSE_MEETING_NOT_FOUND(404, "CS002", "Course Meeting Not Found");


  private final int status;

  private final String code;

  private final String message;


}
