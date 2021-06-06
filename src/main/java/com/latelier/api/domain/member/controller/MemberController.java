package com.latelier.api.domain.member.controller;

import com.latelier.api.domain.member.packet.request.ReqSignUp;
import com.latelier.api.domain.member.packet.response.ResSignUp;
import com.latelier.api.domain.member.service.MemberService;
import com.latelier.api.domain.model.ResultResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

  private final MemberService memberService;


  @PostMapping("/v1/users")
  @ApiOperation(
      value = "사용자 등록",
      notes = "사용자 정보를 등록합니다.",
      response = ResSignUp.class)
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "회원등록 성공"),
      @ApiResponse(responseCode = "409", description = "이메일 또는 휴대폰번호 중복")})
  public ResponseEntity<?> signUpV1(@RequestBody @Valid final ReqSignUp reqSignUp) {

    ResSignUp response = memberService.addMember(reqSignUp);
    return ResponseEntity.ok(
        ResultResponse.<ResSignUp>builder()
            .content(response));
  }

}
