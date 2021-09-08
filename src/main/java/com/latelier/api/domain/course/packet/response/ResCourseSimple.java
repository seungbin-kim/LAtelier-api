package com.latelier.api.domain.course.packet.response;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.course.enumuration.CourseState;
import com.latelier.api.domain.file.entity.File;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResCourseSimple {

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
            value = "강사 이름",
            name = "instructor",
            example = "홍길동")
    private final String instructor;

    @ApiModelProperty(
            value = "강의 가격",
            name = "price",
            example = "10000")
    private final Integer coursePrice;

    @ApiModelProperty(
            value = "강의 최대 인원수",
            name = "maxHeadCount",
            example = "1000")
    private final Integer maxHeadCount;

    @ApiModelProperty(
            value = "강의 현재 인원수",
            name = "currentHeadCount",
            example = "1000")
    private final Integer currentHeadCount;

    @ApiModelProperty(
            value = "강의 상태",
            name = "state",
            example = "APPROVED")
    private final CourseState state;

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


    public static ResCourseSimple of(final Course course,
                                     final File file) {

        return new ResCourseSimple(
                course.getId(),
                course.getCourseName(),
                course.getInstructor().getUsername(),
                course.getCoursePrice(),
                course.getMaxHeadCount(),
                course.getCurrentHeadCount(),
                course.getState(),
                course.getStartDate(),
                course.getEndDate(),
                file != null ? file.getUri() : null);
    }

}
