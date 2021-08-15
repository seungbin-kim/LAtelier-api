package com.latelier.api.global.validation.annotation;

import com.latelier.api.global.validation.validator.Mp4Validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Mp4Validator.class)
public @interface Mp4 {

    String message() default "파일이 없거나 MP4 형식의 동영상이 아닙니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
