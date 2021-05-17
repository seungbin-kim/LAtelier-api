package com.latelier.api.domain.user.packet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@ApiModel("회원가입 요청")
public class ReqSignUp {

  @NotBlank(message = "이름은 필수입니다.")
  @ApiModelProperty(value = "이름", name = "name", notes = "실명을 적어주세요.", example = "홍길동", required = true)
  private String name;

  @NotBlank(message = "전화번호는 필수입니다.")
  @ApiModelProperty(value = "전화번호", name = "phoneNumber", notes = "전화번호를 적어주세요.", example = "01012345678", required = true)
  private String phoneNumber;

  @Email(message = "이메일 형식이 잘못되었습니다.")
  @NotBlank(message = "이메일은 필수입니다.")
  @ApiModelProperty(value = "이메일", name = "email", notes = "이메일을 적어주세요.", example = "test@a.b", required = true)
  private String email;

}
