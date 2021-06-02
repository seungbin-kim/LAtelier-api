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
  INCORRECT_FORMAT(400, "C007", "Incorrect Format"),
  INTERNAL_SERVER_ERROR(500, "C050", "Server Error"),

  // Member
  LOGIN_INPUT_INVALID(400, "M001", "Login input is invalid"),
  EMAIL_DUPLICATION(409, "M002", "Email is Duplication"),
  PHONE_NUMBER_DUPLICATION(409, "M003", "Phone Number is Duplication"),
  SMS_VERIFICATION_FAILED(400, "M004", "Sms Verification Failed"),

  // Zoom
  NOT_OBTAIN_ACCESS_TOKEN(500, "Z001", "Not Obtain Access Token"),
  NOT_OBTAIN_MEETING_INFORMATION(500, "Z002", "Not Obtain Meeting Information"),
  ACCESS_TOKEN_REQUEST_FAILED(500, "Z003", "Access Token Request Failed"),
  ZOOM_API_CALL_FAILED(500, "Z004", "Zoom API Request Failed"),

  // Naver
  SMS_API_CALL_FAILED(500, "N001", "SMS API Request Failed"),

  // Course
  COURSE_NOT_FOUND(404, "CS001", "Course Not Found"),
  COURSE_MEETING_NOT_FOUND(404, "CS002", "Course Meeting Not Found");


  private final int status;

  private final String code;

  private final String message;


}
