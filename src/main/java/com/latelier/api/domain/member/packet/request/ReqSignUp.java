package com.latelier.api.domain.member.packet.request;

import com.latelier.api.global.validator.PhoneNumber;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
@EqualsAndHashCode
@ApiModel("회원가입 요청정보")
public class ReqSignUp {

    @Size(min = 2,
            max = 10,
            message = "이름의 길이는 2 ~ 10글자 이어야 합니다.")
    @NotBlank(message = "이름은 필수입니다.")
    @ApiModelProperty(
            value = "이름",
            name = "username",
            example = "홍길동",
            required = true)
    private String username;

    @PhoneNumber
    @NotBlank(message = "휴대폰번호는 필수입니다.")
    @ApiModelProperty(
            value = "휴대폰번호",
            name = "phoneNumber",
            example = "01000000000",
            required = true)
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

    @NotBlank(message = "회원구분은 필수입니다.")
    @Pattern(regexp = "^(user|instructor|admin)$",
            message = "입력값은 user, instructor, admin 중 하나입니다.")
    @ApiModelProperty(
            value = "회원구분",
            name = "role",
            example = "user",
            required = true)
    private String role;

}