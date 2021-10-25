package com.latelier.api.domain.member.packet.response;

import com.latelier.api.domain.member.entity.Cart;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel("장바구니 강의추가 결과")
public class ResAddCart {

    @ApiModelProperty(
            value = "장바구니 유저 ID",
            name = "memberId",
            example = "1")
    private final Long memberId;

    @ApiModelProperty(
            value = "장바구니 유저이름",
            name = "userName",
            example = "홍길동")
    private final String userName;

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

    public static ResAddCart of(final Cart cart) {

        return new ResAddCart(
                cart.getMember().getId(),
                cart.getMember().getUsername(),
                cart.getCourse().getId(),
                cart.getCourse().getName());
    }

}