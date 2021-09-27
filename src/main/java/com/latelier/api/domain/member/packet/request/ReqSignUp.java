package com.latelier.api.domain.member.packet.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@EqualsAndHashCode
@ApiModel("회원가입 요청정보")
public class ReqSignUp {

    private String username;

    private String phoneNumber;

    @Email(message = "이메일 형식이 잘못되었습니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    @ApiModelProperty(
            value = "이메일",
            name = "email",
            example = "test@a.b",
            required = true)
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$",
            message = "비밀번호는 최소 8자에서 최대 16자, 최소 하나의 문자와 숫자, 특수문자를 포함해야 합니다.")
    @NotBlank(message = "비밀번호는 필수입니다.")
    @ApiModelProperty(
            value = "비밀번호",
            name = "password",
            example = "!mypassword486@",
            required = true)
    private String password;

    private String role;

}