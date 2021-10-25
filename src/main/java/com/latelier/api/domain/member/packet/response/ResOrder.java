package com.latelier.api.domain.member.packet.response;

import com.latelier.api.domain.course.packet.response.ResCourseSimple;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResOrder {

    @ApiModelProperty(
            value = "주문 고유번호",
            name = "id",
            example = "1")
    private final Long id;

    @ApiModelProperty(
            value = "결제금액",
            name = "totalPrice",
            example = "1000")
    private final Integer totalPrice;

    @ApiModelProperty(
            value = "주문일자",
            name = "orderDate",
            example = "2021-10-26T03:32:22.3246389")
    private final LocalDateTime orderDate;

    @ApiModelProperty(
            value = "주문한 강의 목록",
            name = "orderedCourses")
    private final List<ResCourseSimple> orderedCourses;


    public static ResOrder of(final Long orderId,
                              final Integer totalPrice,
                              final LocalDateTime orderDate,
                              final List<ResCourseSimple> courses) {

        return new ResOrder(
                orderId,
                totalPrice,
                orderDate,
                courses);
    }

}
