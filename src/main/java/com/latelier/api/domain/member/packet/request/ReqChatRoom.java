package com.latelier.api.domain.member.packet.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel("채팅방 생성(입장)요청")
public class ReqChatRoom {

    @ApiModelProperty(
            value = "송신자 ID",
            name = "senderId",
            example = "1",
            required = true)
    private Long senderId;

    @ApiModelProperty(
            value = "수신자 ID",
            name = "receiverId",
            example = "2",
            required = true)
    private Long receiverId;

}
