package com.latelier.api.domain.util;

import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MimeTypeChecker {

    private final Tika tika;


    public String checkMimeType(final MultipartFile multipartFile) {

        try {
            return tika.detect(multipartFile.getBytes());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_IO_FAILED);
        }
    }


    public boolean checkImageMimeType(final MultipartFile multipartFile) {

        return checkMimeType(multipartFile).startsWith("image");
    }


    public boolean checkMp4MimeType(final MultipartFile multipartFile) {

        return checkMimeType(multipartFile).equals("video/mp4");
    }

}
