package com.latelier.api.domain.member.packet.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@EqualsAndHashCode
@ApiModel("문자인증번호 확인")
public class ReqSmsVerification {

    @ApiModelProperty(
        value = "휴대폰 번호",
        name = "phoneNumber",
        notes = "인증번호를 받은 휴대폰번호",
        example = "01012345678")
    @Pattern(regexp = "^01[0-9]{8,9}$", message = "올바른 형식이 아닙니다.")
    @NotBlank
    private String phoneNumber;

    @ApiModelProperty(
        value = "인증번호",
        name = "certificationNumber",
        notes = "문자 메세지로 도착한 인증번호 6자리",
        example = "630825")
    @Pattern(regexp = "^[0-9]{6}$", message = "올바른 형식이 아닙니다.")
    @NotBlank
    private String certificationNumber;

}
