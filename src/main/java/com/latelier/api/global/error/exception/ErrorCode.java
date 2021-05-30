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
  INTERNAL_SERVER_ERROR(500, "C006", "Server Error"),

  // Member
  LOGIN_INPUT_INVALID(400, "M001", "Login input is invalid"),
  EMAIL_DUPLICATION(409, "M002", "Email is Duplication"),
  PHONE_NUMBER_DUPLICATION(409, "M003", "Phone Number is Duplication");


  private final int status;

  private final String code;

  private final String message;


}
