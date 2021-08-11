package com.latelier.api.global.validation.validator;

import com.latelier.api.global.validation.ValidPeriod;
import com.latelier.api.global.validation.annotation.Period;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PeriodValidator implements ConstraintValidator<Period, ValidPeriod> {

    @Override
    public boolean isValid(final ValidPeriod validPeriod, final ConstraintValidatorContext context) {
        return validPeriod.isValidDateTime();
    }

}
