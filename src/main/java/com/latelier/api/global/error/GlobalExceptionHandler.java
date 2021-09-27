package com.latelier.api.global.error;

import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Locale;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * javax.validation.Valid or @Validated binding error 발생시
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(final BindException e, final Locale locale) {

        log.error("handleBindException", e);
        final ErrorResponse response = ErrorResponse.of(
                ErrorCode.INVALID_INPUT_VALUE,
                e.getBindingResult(),
                messageSource,
                locale);

        return ResponseEntity.status(BAD_REQUEST)
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

        return ResponseEntity.status(BAD_REQUEST)
                .body(response);
    }


    /**
     * 지원하지 않는 HTTP method 호출 할 경우
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {

        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);

        return ResponseEntity.status(METHOD_NOT_ALLOWED)
                .body(response);
    }


    /**
     * 인증 실패
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(final AuthenticationException e) {

        log.error("handleAuthenticationException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_TOKEN);

        return ResponseEntity.status(valueOf(response.getStatus()))
                .body(response);
    }


    /**
     * 파일 업로드 사이즈 초과
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(final MaxUploadSizeExceededException e) {

        log.error("handleMaxUploadSizeExceededException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.FILE_UPLOAD_SIZE_EXCEEDED);

        return ResponseEntity.status(valueOf(response.getStatus()))
                .body(response);
    }


    /**
     * 필요한 권한이 없는 경우
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(final AccessDeniedException e) {

        log.error("handleAccessDeniedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INSUFFICIENT_SCOPE);

        return ResponseEntity.status(valueOf(response.getStatus()))
                .body(response);
    }


    /**
     * JSON 요청 형태가 아닌 경우
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {

        log.error("handleHttpMessageNotReadableException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INCORRECT_FORMAT);

        return ResponseEntity.status(valueOf(response.getStatus()))
                .body(response);
    }


    /**
     * 처리 핸들러를 찾지 못한경우
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoHandlerFoundException(final NoHandlerFoundException e) {

        log.error("handleNoHandlerFoundException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_FOUND);

        return ResponseEntity.status(valueOf(response.getStatus()))
                .body(response);
    }


    /**
     * 비즈니스 예외
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {

        log.error("handleBusinessException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);

        return ResponseEntity.status(valueOf(errorCode.getStatus()))
                .body(response);
    }


    @ExceptionHandler(IamportResponseException.class)
    protected ResponseEntity<ErrorResponse> handleIamportResponseException(final IamportResponseException e) {

        log.error("handleIamportResponseException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.IAMPORT_ERROR);

        return ResponseEntity.status(valueOf(response.getStatus()))
                .body(response);
    }


    /**
     * 나머지 모든 예외
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(final Exception e) {

        log.error("handleException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(response);
    }

}
