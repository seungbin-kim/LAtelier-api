package com.latelier.api.domain.member.packet.response;

import com.latelier.api.domain.file.entity.File;
import com.latelier.api.domain.member.entity.Cart;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ApiModel("장바구니 목록 요소")
public class ResMyCart {

    @ApiModelProperty(
            value = "요소의 ID",
            name = "id",
            example = "1")
    private final Long id;

    @ApiModelProperty(
            value = "강의 ID",
            name = "courseId",
            example = "1")
    private final Long courseId;

    @ApiModelProperty(
            value = "강의 이름",
            name = "courseName",
            example = "재미있는 강의")
    private final String courseName;

    @ApiModelProperty(
            value = "가격",
            name = "coursePrice",
            example = "10000")
    private final Integer coursePrice;

    @ApiModelProperty(
            value = "다운로드 주소",
            name = "thumbnailImageUri",
            example = "https://blabla...")
    private final String thumbnailImageUri;


    public static ResMyCart of(final Cart cart, final File file) {

        return new ResMyCart(
                cart.getId(),
                cart.getCourse().getId(),
                cart.getCourse().getCourseName(),
                cart.getCourse().getCoursePrice(),
                file.getUri());
    }

}
