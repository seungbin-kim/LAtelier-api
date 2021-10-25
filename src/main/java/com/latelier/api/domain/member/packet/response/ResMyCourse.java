package com.latelier.api.domain.member.packet.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.file.entity.File;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResMyCourse {

    @ApiModelProperty(
            value = "강의 종료여부",
            name = "hasEnded",
            example = "false")
    private final boolean hasEnded;

    @ApiModelProperty(
            value = "강의 고유번호",
            name = "id",
            example = "1")
    private final Long id;

    @ApiModelProperty(
            value = "강의 이름",
            name = "courseName",
            example = "my course")
    private final String courseName;

    @ApiModelProperty(
            value = "강의 최대 인원수",
            name = "maxHeadCount",
            example = "1000")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer maxHeadCount;

    @ApiModelProperty(
            value = "강의 현재 인원수",
            name = "currentHeadCount",
            example = "1000")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer currentHeadCount;

    @ApiModelProperty(
            value = "강의 시작일",
            name = "startDate",
            example = "2021-08-08T18:00:00")
    private final LocalDateTime startDate;

    @ApiModelProperty(
            value = "강의 종료일",
            name = "endDate",
            example = "2021-09-10T18:00:00")
    private final LocalDateTime endDate;

    @ApiModelProperty(
            value = "썸네일 이미지 URI",
            name = "thumbnailImage")
    private final String thumbnailImageUri;


    public static ResMyCourse forInstructor(final Course course,
                                            final File file) {

        return new ResMyCourse(
                course.hasEnded(),
                course.getId(),
                course.getName(),
                course.getMaxHeadCount(),
                course.getCurrentHeadCount(),
                course.getStartDate(),
                course.getEndDate(),
                file.getUri());
    }

    public static ResMyCourse forMember(final Course course,
                                        final File file) {

        return new ResMyCourse(
                course.hasEnded(),
                course.getId(),
                course.getName(),
                null,
                null,
                course.getStartDate(),
                course.getEndDate(),
                file.getUri());
    }

}
