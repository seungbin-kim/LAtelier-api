package com.latelier.api.domain.member.packet.response;

import com.latelier.api.domain.member.entity.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel("로그인 결과")
public class ResSignIn {

    @ApiModelProperty(
            value = "이름",
            name = "username",
            example = "홍길동")
    private final String username;

    @ApiModelProperty(
            value = "닉네임",
            name = "nickname",
            example = "똥이")
    private final String nickname;

    @ApiModelProperty(
            value = "이메일",
            name = "email",
            example = "test@a.b")
    private final String email;


    @ApiModelProperty(
            value = "회원구분",
            name = "role",
            example = "user")
    private final String role;


    public static ResSignIn of(final Member member) {

        return new ResSignIn(
                member.getUsername(),
                member.getNickname(),
                member.getEmail(),
                member.getAuthority().getRoleName());
    }

}
