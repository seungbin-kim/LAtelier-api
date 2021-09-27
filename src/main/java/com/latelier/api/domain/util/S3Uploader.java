package com.latelier.api.domain.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.latelier.api.global.error.exception.BusinessException;
import com.latelier.api.global.error.exception.ErrorCode;
import com.latelier.api.global.properties.CloudProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    private final MimeTypeChecker mimeTypeChecker;

    private final CloudProperties cloudProperties;


    /**
     * 파일을 S3 버킷에 업로드하고, 업로드 결과를 반환합니다.
     *
     * @param multipartFile 업로드 파일
     * @param path          저장 경로
     * @return 업로드된 주소, 파일이름, 저장된파일이름, 파일 사이즈가 담긴 결과 객체
     */
    public UploadResult upload(final MultipartFile multipartFile, final String path) {

        if (!StringUtils.hasText(multipartFile.getOriginalFilename())) {
            throw new BusinessException(ErrorCode.FILE_NOT_EXISTED);
        }

        final String truncatedFilename = truncateFilename(multipartFile.getOriginalFilename());
        final String storedFileName = getUUID() + truncatedFilename;
        final String fullPath = path + storedFileName;

        long fileSize = putS3(multipartFile, fullPath);
        String uri = amazonS3Client.getUrl(cloudProperties.getAws().getS3().getBucket(), fullPath).toString();
        return new UploadResult(uri, truncatedFilename, storedFileName, fileSize);
    }


    /**
     * S3 버킷에 파일을 저장합니다.
     *
     * @param multipartFile 사용자로부터 받은 파일
     * @param fullPath      저장 경로
     * @return 저장된 파일의 크기(byte)
     */
    private long putS3(final MultipartFile multipartFile, final String fullPath) {

        try {
            final long fileSize = multipartFile.getSize();
            final ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(mimeTypeChecker.checkMimeType(multipartFile));
            metadata.setContentLength(fileSize);

            amazonS3Client.putObject(new PutObjectRequest(
                    cloudProperties.getAws().getS3().getBucket(),
                    fullPath, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            return fileSize;
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_IO_FAILED);
        }
    }


    /**
     * 파일 이름을 최대 50자까지 자릅니다.
     *
     * @param originalFilename 원본파일 이름
     * @return 50자 이하라면 인자로 넘긴 값 그대로, 50자 초과라면 50자까지 자른 문자열
     */
    private String truncateFilename(final String originalFilename) {

        return originalFilename.length() > 50 ? originalFilename.substring(0, 50) : originalFilename;
    }


    private String getUUID() {
        
        return UUID.randomUUID().toString();
    }


    /**
     * S3 업로드 결과 객체
     */
    @Getter
    @RequiredArgsConstructor
    public static class UploadResult {
        /**
         * 업로드 된 전체 주소입니다.
         */
        private final String uri;

        /**
         * 최대 50자 길이의 원본파일 이름입니다.
         */
        private final String originalFilename;

        /**
         * S3 버킷에 저장된 파일 이름입니다.
         */
        private final String storedFilename;

        /**
         * 파일의 크기(byte)
         */
        private final long fileSize;
    }

}

