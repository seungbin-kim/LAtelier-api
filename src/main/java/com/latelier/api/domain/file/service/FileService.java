package com.latelier.api.domain.file.service;

import com.latelier.api.domain.file.entity.File;
import com.latelier.api.domain.file.enumuration.FileGroup;
import com.latelier.api.domain.file.repository.FileRepository;
import com.latelier.api.domain.util.S3Uploader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.latelier.api.domain.util.S3Uploader.UploadResult;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final S3Uploader s3Uploader;

    private final FileRepository fileRepository;


    /**
     * 파일을 업로드하고, 업로드한 파일 정보를 바탕으로 엔티티를 생성합니다.
     *
     * @param willPerpetuated 업로드 정보를 영속화까지 시킬것인지 여부
     * @param identifier      식별자(폴더)
     * @param uploadRequests  업로드 요청 객체(파일, 파일의 분류)
     * @return File 엔티티 리스트
     */
    @Transactional
    public List<File> uploadFile(final boolean willPerpetuated,
                                 final Long identifier,
                                 final String basePath,
                                 final UploadRequest... uploadRequests) {

        final String uploadPath = basePath + identifier + "/";
        List<File> fileList = Arrays.stream(uploadRequests)
                .map(request -> {
                    UploadResult result = s3Uploader.upload(request.multipartFile, uploadPath);
                    return File.of(
                            request.getFileGroup(),
                            result.getOriginalFilename(),
                            result.getStoredFilename(),
                            result.getUri(),
                            result.getFileSize());
                })
                .collect(Collectors.toList());
        return willPerpetuated ? fileRepository.saveAll(fileList) : fileList;
    }


    /**
     * 업로드 요청 DTO
     */
    @Getter
    @RequiredArgsConstructor
    public static class UploadRequest {
        /**
         * 업로드 할 파일
         */
        private final MultipartFile multipartFile;

        /**
         * 파일에 대한 구분
         */
        private final FileGroup fileGroup;

    }

}
