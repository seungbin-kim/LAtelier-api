package com.latelier.api.global.validation.validator;

import com.latelier.api.domain.util.MimeTypeChecker;
import com.latelier.api.global.validation.annotation.Mp4;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class Mp4Validator implements ConstraintValidator<Mp4, MultipartFile> {

    private final MimeTypeChecker mimeTypeChecker;

    @Override
    public boolean isValid(final MultipartFile multipartFile, final ConstraintValidatorContext context) {

        if (multipartFile == null || multipartFile.getContentType() == null || multipartFile.isEmpty()) {
            return false;
        }
        return mimeTypeChecker.checkMp4MimeType(multipartFile);
    }

}
