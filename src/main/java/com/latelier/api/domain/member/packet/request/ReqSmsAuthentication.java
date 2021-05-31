package com.latelier.api.domain.member.packet.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@EqualsAndHashCode
@ApiModel("문자인증 요청")
public class ReqSmsAuthentication {

  @ApiModelProperty(
      value = "휴대폰 번호",
      name = "phoneNumber",
      notes = "인증에 필요한 휴대폰 번호",
      example = "01012345678")
  @Pattern(regexp = "^01[0-9]{8,9}$", message = "올바른 형식이 아닙니다.")
  @NotBlank
  private String phoneNumber;

}
