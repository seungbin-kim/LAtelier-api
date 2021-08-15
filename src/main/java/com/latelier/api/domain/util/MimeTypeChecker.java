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


    /**
     * MimeType 타입을 체크합니다.
     *
     * @param multipartFile 확인할 파일
     * @return MimeType 문자열
     */
    public String checkMimeType(final MultipartFile multipartFile) {

        try {
            return tika.detect(multipartFile.getBytes());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_IO_FAILED);
        }
    }


    /**
     * 이미지 형식의 파일인지 확인합니다.
     *
     * @param multipartFile 확인할 파일
     * @return 이미지 형식이라면 true, 아니라면 false
     */
    public boolean checkImageMimeType(final MultipartFile multipartFile) {

        return checkMimeType(multipartFile).startsWith("image");
    }


    /**
     * mp4 타입의 동영상 파일인지 확인합니다.
     *
     * @param multipartFile 확인할 파일
     * @return mp4 형식의 동영상 이라면 true, 아니라면 false
     */
    public boolean checkMp4MimeType(final MultipartFile multipartFile) {

        return checkMimeType(multipartFile).equals("video/mp4");
    }

}
