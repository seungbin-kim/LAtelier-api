package com.latelier.api.domain.member.packet.response;

import com.latelier.api.domain.member.entity.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel("회원가입 요청결과")
public class ResSignUp {

  @ApiModelProperty(
      value = "이름",
      name = "name",
      example = "홍길동")
  private final String name;

  @ApiModelProperty(
      value = "전화번호",
      name = "phoneNumber",
      example = "01000000000")
  private final String phoneNumber;

  @ApiModelProperty(
      value = "이메일",
      name = "email",
      example = "test@a.b")
  private final String email;

  @ApiModelProperty(
      value = "닉네임",
      name = "nickName",
      example = "myNickName")
  private final String nickName;


  public static ResSignUp createSignUpResponse(final Member member) {

    return
        new ResSignUp(
            member.getName(),
            member.getPhoneNumber(),
            member.getEmail(),
            member.getNickname());
  }

}