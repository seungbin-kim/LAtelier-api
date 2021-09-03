package com.latelier.api.domain.course.packet.response;

import com.latelier.api.domain.course.entity.Category;
import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.file.entity.File;
import com.latelier.api.domain.file.packet.response.ResFileInformation;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResCourseRegister {

    @ApiModelProperty(
            value = "강의 이름",
            name = "courseName",
            example = "my course")
    private final String courseName;

    @ApiModelProperty(
            value = "강의 설명",
            name = "explanation",
            example = "재미있는 수업")
    private final String explanation;

    @ApiModelProperty(
            value = "강의 가격",
            name = "coursePrice",
            example = "10000")
    private final Integer coursePrice;

    @ApiModelProperty(
            value = "강의 최대 인원수",
            name = "headCount",
            example = "1000")
    private final Integer headCount;

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
            value = "등록된 카테고리",
            name = "categories")
    private final List<ResCategory> categories;

    @ApiModelProperty(
            value = "업로드 된 파일정보",
            name = "uploadedFiles")
    private final List<ResFileInformation> uploadedFiles;


    public static ResCourseRegister of(final Course course,
                                       final List<Category> categories,
                                       final List<File> files) {

        return new ResCourseRegister(
                course.getCourseName(),
                course.getExplanation(),
                course.getPrice(),
                course.getMaxSize(),
                course.getStartDate(),
                course.getEndDate(),
                categories.stream()
                        .map(ResCategory::withoutSubCategoriesOf)
                        .collect(Collectors.toList()),
                files.stream()
                        .map(ResFileInformation::of)
                        .collect(Collectors.toList()));
    }

}
