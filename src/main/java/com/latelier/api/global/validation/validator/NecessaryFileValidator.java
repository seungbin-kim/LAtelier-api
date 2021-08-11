package com.latelier.api.global.validation.validator;

import com.latelier.api.global.validation.annotation.NecessaryFile;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NecessaryFileValidator implements ConstraintValidator<NecessaryFile, MultipartFile> {

    @Override
    public boolean isValid(final MultipartFile multipartFile, final ConstraintValidatorContext context) {

        return multipartFile != null && multipartFile.getContentType() != null && !multipartFile.isEmpty();
    }
}
