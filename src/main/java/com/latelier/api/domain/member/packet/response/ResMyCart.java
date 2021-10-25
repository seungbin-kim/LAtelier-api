package com.latelier.api.domain.member.packet.response;

import com.latelier.api.domain.course.entity.Course;
import com.latelier.api.domain.file.entity.File;
import com.latelier.api.domain.member.entity.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel("장바구니")
public class ResMyCart {

    @ApiModelProperty(
            value = "장바구니에 담긴 강의들",
            name = "list")
    private final List<CartElement> list;

    @ApiModelProperty(
            value = "전체 가격",
            name = "totalPrice",
            example = "1000")
    private final Integer totalPrice;

    @ApiModelProperty(
            value = "장바구니 이름(구매 이름)",
            name = "name",
            example = "XXX 외 2건")
    private final String name;

    @ApiModelProperty(
            value = "유저 고유번호",
            name = "userId",
            example = "1")
    private final Long userId;

    @ApiModelProperty(
            value = "유저 이름",
            name = "userName",
            example = "홍길동")
    private final String userName;

    @ApiModelProperty(
            value = "유저 email",
            name = "email",
            example = "abc123@a.b")
    private final String email;

    @ApiModelProperty(
            value = "유저 휴대폰번호",
            name = "phoneNumber",
            example = "01011112222")
    private final String phoneNumber;


    public static ResMyCart of(final List<CartElement> cartList,
                               final Integer totalPrice,
                               final Member member) {

        int elementSize = cartList.size();
        final String name =
                elementSize == 0 ? "" : elementSize == 1 ?
                        cartList.get(0).courseName : cartList.get(0).courseName + " 외 " + (elementSize - 1) + "개";

        return new ResMyCart(
                cartList,
                totalPrice,
                name,
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
                value = "다운로드 주소",
                name = "thumbnailImageUri",
                example = "https://blabla...")
        private final String thumbnailImageUri;


        public static CartElement of(final Course course,
                                     final File file) {

            return new CartElement(
                    course.getId(),
                    course.getName(),
                    course.getPrice(),
                    course.getStartDate(),
                    course.getEndDate(),
                    file.getUri());
        }
    }

}
