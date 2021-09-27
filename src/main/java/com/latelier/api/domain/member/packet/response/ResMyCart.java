package com.latelier.api.domain.member.packet.response;

import com.latelier.api.domain.file.entity.File;
import com.latelier.api.domain.member.entity.Cart;
import com.latelier.api.domain.member.entity.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel("장바구니 목록")
public class ResMyCart {

    @ApiModelProperty(
            value = "강의 고유번호",
            name = "id",
            example = "1")
    private final List<CartElement> list;

    private final Integer totalPrice;

    private final String title;

    private final Long userId;

    private final String userName;

    private final String email;

    private final String phoneNumber;


    public static ResMyCart of(final List<CartElement> cartList,
                               final Integer totalPrice,
                               final Member member) {

        int elementSize = cartList.size();
        final String title =
                elementSize == 0 ? "" : elementSize == 1 ?
                        cartList.get(0).courseName : cartList.get(0).courseName + " 외 " + elementSize + "개";
        return new ResMyCart(
                cartList,
                totalPrice,
                title,
                member.getId(),
                member.getUsername(),
                member.getEmail(),
                member.getPhoneNumber());
    }


    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel("장바구니 목록 요소")
    public static class CartElement {

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


        public static CartElement of(final Cart cart, final File file) {

            return new CartElement(
                    cart.getId(),
                    cart.getCourse().getId(),
                    cart.getCourse().getCourseName(),
                    cart.getCourse().getCoursePrice(),
                    file.getUri());
        }
    }

}
