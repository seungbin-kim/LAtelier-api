package com.latelier.api.domain.course.packet.request;

import com.latelier.api.global.validation.ValidPeriod;
import com.latelier.api.global.validation.annotation.Image;
import com.latelier.api.global.validation.annotation.Mp4;
import com.latelier.api.global.validation.annotation.Period;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Period
@ApiModel("강의등록 폼")
public class ReqCourseRegister implements ValidPeriod {

    @NotBlank(message = "강의 이름은 필수입니다.")
    @Size(min = 2,
            max = 50,
            message = "강의 이름은 2 ~ 50글자 이어야 합니다.")
    @ApiModelProperty(
            value = "강의 이름",
            name = "courseName",
            example = "my course",
            required = true)
    private String courseName;

    @NotBlank(message = "강의 설명은 필수입니다.")
    @Size(min = 1,
            max = 500,
            message = "강의 설명은 최대 500글자 입니다.")
    @ApiModelProperty(
            value = "강의 설명",
            name = "explanation",
            example = "재미있는 수업",
            required = true)
    private String explanation;

    @NotNull(message = "강의 가격은 필수입니다.")
    @Min(value = 0,
            message = "강의 가격은 0 이상입니다.")
    @Max(value = 10_000_000,
            message = "강의 가격은 최대 1000만원(10000000) 입니다.")
    @ApiModelProperty(
            value = "강의 가격",
            name = "coursePrice",
            example = "10000",
            required = true)
    private Integer coursePrice;

    @NotNull(message = "강의 최대 인원수는 필수입니다.")
    @Min(value = 0,
            message = "강의 최대 인원수는 0 이상입니다.")
    @Max(value = 1_000,
            message = "강의 최대 인원수는 최대 1000 입니다.")
    @ApiModelProperty(
            value = "강의 최대 인원수",
            name = "headCount",
            example = "1000",
            required = true)
    private Integer headCount;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "강의 시작일은 필수입니다.")
    @ApiModelProperty(
            value = "강의 시작일",
            name = "startDate",
            example = "2021-08-08T18:00",
            required = true)
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "강의 종료일은 필수입니다.")
    @ApiModelProperty(
            value = "강의 종료일",
            name = "endDate",
            example = "2021-09-10T18:00",
            required = true)
    private LocalDateTime endDate;

    @Image
    @ApiModelProperty(
            value = "강의 역량을 나타내는 결과물 사진",
            name = "proofImageFile",
            required = true)
    private MultipartFile proofImageFile;

    @Image
    @ApiModelProperty(
            value = "썸네일 이미지 파일",
            name = "thumbnailImageFile",
            required = true)
    private MultipartFile thumbnailImageFile;

    @Image
    @ApiModelProperty(
            value = "상세설명 이미지 파일",
            name = "detailImageFile",
            required = true)
    private MultipartFile detailImageFile;

    @Mp4
    @ApiModelProperty(
            value = "강의소개 동영상 파일",
            name = "videoFile",
            required = true)
    private MultipartFile videoFile;

    @NotNull(message = "카테고리는 필수입니다.")
    @ApiModelProperty(
            value = "강의에 해당하는 카테고리들",
            name = "categoryIds",
            required = true)
    private List<Long> categoryIds;


    @Override
    @ApiModelProperty(hidden = true)
    public boolean isValidDateTime() {
        if (startDate == null || endDate == null) {
            return false;
        }

        return endDate.isAfter(startDate);
    }

}


