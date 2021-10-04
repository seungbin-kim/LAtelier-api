package com.latelier.api.domain.member.controller;

import com.latelier.api.domain.member.service.OrderService;
import com.latelier.api.domain.util.SecurityUtil;
import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentController {

    private final OrderService orderService;

    private final SecurityUtil securityUtil;


    @PostMapping("/payment-verification")
    @ApiOperation(
            value = "결제 처리",
            notes = "결제 금액을 검증하고, 주문을 처리합니다.",
            authorizations = {@Authorization(value = "jwt")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "결제 검증, 주문처리 성공"),
            @ApiResponse(responseCode = "400", description = "주문한 사용자가 다르거나 위조된 결제"),
            @ApiResponse(responseCode = "401", description = "로그인하지 않아 주문처리 불가"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "결제 검증 응답에러 또는 서버에러")})
    public ResponseEntity<Void> verifyOrder(@RequestBody @Valid final Verification verification,
                                            @RequestParam(defaultValue = "false") final boolean isTest)
            throws IamportResponseException, IOException {

//        orderService.verifyAndProcess(securityUtil.getMemberId(), verification.userId, verification.impUid, isTest && securityUtil.isAdmin());
        orderService.verifyAndProcess(1L, verification.userId, verification.impUid, isTest && securityUtil.isAdmin());
        return ResponseEntity.noContent().build();
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
