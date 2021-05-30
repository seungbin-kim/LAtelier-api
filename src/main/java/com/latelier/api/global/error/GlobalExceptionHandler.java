package com.latelier.api.global.error;

import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * javax.validation.Valid or @Validated binding error 발생시
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {

    log.error("handleMethodArgumentNotValidException", e);
    final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
    return
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
  }


  /**
   * enum type 일치하지 않아 binding 못할 경우
   * 주로 @RequestParam enum 으로 binding 못했을 경우 발생
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {

    log.error("handleMethodArgumentTypeMismatchException", e);
    final ErrorResponse response = ErrorResponse.of(e);
    return
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
  }


  /**
   * 지원하지 않는 HTTP method 호출 할 경우
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {

    log.error("handleHttpRequestMethodNotSupportedException", e);
    final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
    return
        ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(response);
  }


  /**
   * 권한 자체가 없는 경우
   */
  @ExceptionHandler(AuthenticationException.class)
  protected ResponseEntity<ErrorResponse> handleAccessDeniedException(final AuthenticationException e) {

    log.error("handleAuthenticationException", e);
    final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_TOKEN);
    return
        ResponseEntity
            .status(HttpStatus.valueOf(ErrorCode.INVALID_TOKEN.getStatus()))
            .body(response);
  }


  /**
   * Authentication 객체가 필요한 권한이 없는 경우
   */
  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<ErrorResponse> handleAccessDeniedException(final AccessDeniedException e) {

    log.error("handleAccessDeniedException", e);
    final ErrorResponse response = ErrorResponse.of(ErrorCode.INSUFFICIENT_SCOPE);
    return
        ResponseEntity
            .status(HttpStatus.valueOf(ErrorCode.INSUFFICIENT_SCOPE.getStatus()))
            .body(response);
  }


  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {

    log.error("handleBusinessException", e);
    final ErrorCode errorCode = e.getErrorCode();
    final ErrorResponse response = ErrorResponse.of(errorCode);
    return
        ResponseEntity
            .status(HttpStatus.valueOf(errorCode.getStatus()))
            .body(response);
  }


  /**
   * 나머지 모든 예외
   */
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(final Exception e) {

    log.error("handleException", e);
    final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
    return
        ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
  }

}
