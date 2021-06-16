package com.latelier.api.domain.member.packet.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@ApiModel("Json Web Token")
@RequiredArgsConstructor
public class ResToken {

  @ApiModelProperty(
      value = "JWT",
      name = "token",
      example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
          ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ" +
          ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
  private final String token;

}
