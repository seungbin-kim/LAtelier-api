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
  UNAUTHORIZED(401, "C004", "Unauthorized"),
  INSUFFICIENT_SCOPE(403, "C005", "Insufficient Scope"),
  NOT_FOUND(404, "C006", "Not Found"),
//  DUPLICATION(409, "C007", "Duplicate"),
  INCORRECT_FORMAT(400, "C008", "Incorrect Format"),
  INTERNAL_SERVER_ERROR(500, "C050", "Server Error"),

  // Member
  MEMBER_NOT_FOUND(404, "M001", "Not Found Member"),
  LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),
  SMS_VERIFICATION_FAILED(400, "M003", "Sms Verification Failed"),
  EMAIL_DUPLICATE(409, "M004", "Duplicate Email"),
  PHONE_NUMBER_DUPLICATE(409, "M005", "Duplicate Phone Number"),
  EMAIL_AND_PHONE_NUMBER_DUPLICATE(409, "M006", "Duplicate Email And Phone Number"),
  EMAIL_NOT_FOUND(404, "M007", "Not Found Email"),
  NOT_ACTIVATED(404, "M010", "Not Activated"),

  // Zoom
  ACCESS_TOKEN_NOT_OBTAIN(500, "Z001", "Not Obtain Access Token"),
  MEETING_INFORMATION_NOT_OBTAIN(500, "Z002", "Not Obtain Meeting Information"),
  ACCESS_TOKEN_REQUEST_FAILED(500, "Z003", "Access Token Request Failed"),
  ZOOM_API_CALL_FAILED(500, "Z004", "Zoom API Request Failed"),

  // Naver
  SMS_API_CALL_FAILED(500, "N001", "SMS API Request Failed"),

  // Course
  COURSE_NOT_FOUND(404, "CS001", "Course Not Found"),
  COURSE_MEETING_NOT_FOUND(404, "CS002", "Course Meeting Not Found"),

  // CHAT
  CHAT_ROOM_NOT_FOUND(404, "CH001", "Chat Room Not Found");

  private final int status;

  private final String code;

  private final String message;


}
