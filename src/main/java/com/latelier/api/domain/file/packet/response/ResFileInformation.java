package com.latelier.api.domain.file.packet.response;

import com.latelier.api.domain.file.entity.File;
import com.latelier.api.domain.file.enumuration.FileGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel("파일 정보")
public class ResFileInformation {

    @ApiModelProperty(
            value = "파일 ID",
            name = "id",
            example = "1")
    private final Long id;

    @ApiModelProperty(
            value = "파일그룹 이름",
            name = "fileGroup",
            example = "COURSE_THUMBNAIL_IMAGE")
    private final FileGroup fileGroup;

    @ApiModelProperty(
            value = "원본 파일이름",
            name = "originalFilename",
            example = "my_file.png")
    private final String originalFilename;

    @ApiModelProperty(
            value = "저장된 파일이름",
            name = "storedFileName",
            example = "f93e8e8e-1c09-04d30-94fd-a191aae1a3d6my_file.png")
    private final String storedFilename;

    @ApiModelProperty(
            value = "다운로드 주소",
            name = "uri",
            example = "https://blabla...")
    private final String uri;

    @ApiModelProperty(
            value = "파일 사이즈(byte)",
            name = "fileSize",
            example = "1000000")
    private final Long fileSize;


    public static ResFileInformation of(final File file) {

        return new ResFileInformation(
                file.getId(),
                file.getFileGroup(),
                file.getOriginalFilename(),
                file.getStoredFilename(),
                file.getUri(),
                file.getFileSize());
    }

}
