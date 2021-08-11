package com.latelier.api.global.validation.validator;

import com.latelier.api.domain.util.MimeTypeChecker;
import com.latelier.api.global.validation.annotation.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {

    private final MimeTypeChecker mimeTypeChecker;

    @Override
    public boolean isValid(final MultipartFile multipartFile, final ConstraintValidatorContext context) {

        if (multipartFile == null || multipartFile.getContentType() == null || multipartFile.isEmpty()) {
            return false;
        }
        return mimeTypeChecker.checkImageMimeType(multipartFile);
    }

}
