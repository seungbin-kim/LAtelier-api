package com.latelier.api.controller;

import com.latelier.api.domain.member.packet.response.ResOrder;
import com.latelier.api.domain.member.service.OrderService;
import com.latelier.api.domain.model.Result;
import com.latelier.api.domain.util.SecurityUtil;
import com.latelier.api.global.error.ErrorResponse;
import com.latelier.api.global.error.exception.ErrorCode;
import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    private final SecurityUtil securityUtil;


    @PostMapping("/payment-verification")
    @ApiOperation(
            value = "결제 처리",
            notes = "결제 금액을 검증하고, 주문을 처리합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제 검증, 주문처리 성공"),
            @ApiResponse(responseCode = "400", description = "결제중 인원이 초과되거나 종료된 강의가 있어 결제가 취소됨"),
            @ApiResponse(responseCode = "400", description = "주문한 사용자가 달라 결제가 취소됨"),
            @ApiResponse(responseCode = "400", description = "위조된 결제. 결제가 취소됨"),
            @ApiResponse(responseCode = "401", description = "로그인하지 않아 주문처리 불가"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "결제 검증시 API 호출에러 또는 서버에러")})
    public ResponseEntity<?> verifyOrder(@RequestBody @Valid final Verification verification,
                                         @RequestParam(defaultValue = "false") final boolean isTest) throws IamportResponseException, IOException {

        ResOrder resOrder = orderService.verifyAndProcess(
                securityUtil.getMemberId(), verification.userId,
                verification.impUid, isTest && securityUtil.isAdmin());
        if (resOrder == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.of(ErrorCode.PAYMENT_CANCEL));
        return ResponseEntity.ok(Result.of(resOrder));
    }


    @Getter
    @ApiModel("결제정보 확인")
    static class Verification {

        @NotNull(message = "구매자 ID는 필수입니다.")
        private Long userId;

        @NotBlank(message = "필수입니다.")
        private String impUid;

    }

}
