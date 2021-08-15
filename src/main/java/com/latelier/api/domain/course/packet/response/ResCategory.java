package com.latelier.api.domain.course.packet.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.latelier.api.domain.course.entity.Category;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel("카테고리 목록")
public class ResCategory {

    @ApiModelProperty(
            value = "카테고리 ID",
            name = "id",
            example = "1")
    private final Long id;

    @ApiModelProperty(
            value = "카테고리 이름",
            name = "categoryName",
            example = "라이프 스타일")
    private final String categoryName;

    @ApiModelProperty(
            value = "하위 카테고리",
            name = "subCategories")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<ResCategory> subCategories;


    public static ResCategory of(final Category category) {

        return new ResCategory(
                category.getId(),
                category.getCategoryName(),
                category.getSubCategories().stream()
                        .map(ResCategory::of)
                        .collect(Collectors.toList()));
    }


    public static ResCategory withoutSubCategoriesOf(final Category category) {

        return new ResCategory(
                category.getId(),
                category.getCategoryName(),
                null);
    }

}
