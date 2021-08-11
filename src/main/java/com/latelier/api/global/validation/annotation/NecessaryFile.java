package com.latelier.api.global.validation.annotation;

import com.latelier.api.global.validation.validator.NecessaryFileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NecessaryFileValidator.class)
public @interface NecessaryFile {

    String message() default "필수 파일입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
