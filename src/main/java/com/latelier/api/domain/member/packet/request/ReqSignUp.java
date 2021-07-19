package com.latelier.api.domain.member.packet.request;

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
            name = "name",
            example = "홍길동",
            required = true)
    private String name;

    @Pattern(regexp = "^01[0-9]{8,9}$",
            message = "올바른 형식이 아닙니다.")
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

    @Size(max = 500,
            message = "최대 500자 입니다.")
    @ApiModelProperty(
            value = "자기소개",
            name = "introduction",
            example = "안녕하세요.")
    private String introduction;

    @Size(max = 100,
            message = "주소는 최대 100자 입니다.")
    @ApiModelProperty(
            value = "주소",
            name = "address",
            example = "지구 광역시 나맞구 일로 53")
    private String address;

    @Size(max = 10,
            message = "우편번호는 최대 10자 입니다.")
    @ApiModelProperty(
            value = "우편번호",
            name = "zipCode",
            example = "65536")
    private String zipCode;

    @NotBlank(message = "강사여부는 필수입니다.")
    @Pattern(regexp = "^(true|false)$", message = "입력값은 true 혹은 false 중 하나입니다.")
    @ApiModelProperty(
            value = "강사여부",
            name = "isTeacher",
            example = "false",
            required = true)
    private String isTeacher;

}
