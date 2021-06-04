package com.latelier.api.domain.member.controller;

import com.latelier.api.domain.member.packet.request.ReqSignUp;
import io.swagger.annotations.ApiOperation;
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

  @PostMapping("/v1/users")
  @ApiOperation(
      value = "사용자 등록",
      notes = "사용자 정보를 등록합니다.")
  public ResponseEntity<?> signUpV1(@RequestBody @Valid final ReqSignUp reqSignUp) {

    return ResponseEntity.noContent().build();
  }

}
