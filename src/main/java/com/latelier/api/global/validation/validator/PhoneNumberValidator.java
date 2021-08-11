package com.latelier.api.global.validation.validator;

import com.latelier.api.global.validation.annotation.PhoneNumber;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    final String phoneNumberRegex = "^01[0-9]{8,9}$";


    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {

        if (!StringUtils.hasText(value)) {
            return false;
        }
        Pattern pattern = Pattern.compile(phoneNumberRegex);
        return pattern.matcher(value).matches();
    }

}
