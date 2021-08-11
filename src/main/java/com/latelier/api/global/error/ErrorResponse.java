package com.latelier.api.global.error;

import com.latelier.api.global.error.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String message;

    private int status;

    private List<Error> errors;

    private String code;


    private ErrorResponse(final ErrorCode code, final List<Error> errors) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.errors = errors;
        this.code = code.getCode();
    }


    private ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.errors = new ArrayList<>();
    }


    public static ErrorResponse of(final ErrorCode code,
                                   final BindingResult bindingResult,
                                   final MessageSource messageSource,
                                   final Locale locale) {

        return new ErrorResponse(code, Error.of(bindingResult, messageSource, locale));
    }


    public static ErrorResponse of(final ErrorCode code) {

        return new ErrorResponse(code);
    }


    public static ErrorResponse of(final ErrorCode code,
                                   final List<Error> errors) {

        return new ErrorResponse(code, errors);
    }


    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {

        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<Error> errors = Error.of(e.getName(), value, e.getErrorCode());

        return new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE, errors);
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Error {

        private String target;

        private String value;

        private String reason;


        private Error(final String target,
                      final String value,
                      final String reason) {

            this.target = target;
            this.value = value.contains("@") ? "" : value;
            this.reason = reason;
        }


        public static List<Error> of(final String target,
                                     final String value,
                                     final String reason) {

            List<Error> errors = new ArrayList<>();
            errors.add(new Error(target, value, reason));
            return errors;
        }


        private static List<Error> of(final BindingResult bindingResult,
                                      final MessageSource messageSource,
                                      final Locale locale) {

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            if (!fieldErrors.isEmpty()) {
                return fieldErrors.stream()
                        .map(error -> new Error(
                                error.getField(),
                                error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                                messageSource.getMessage(error, locale)))
                        .collect(Collectors.toList());
            }

            List<ObjectError> allErrors = bindingResult.getAllErrors();
            return allErrors.stream()
                    .map(objectError -> new Error(
                            objectError.getObjectName(),
                            "",
                            objectError.getDefaultMessage()))
                    .collect(Collectors.toList());
        }

    }

}
