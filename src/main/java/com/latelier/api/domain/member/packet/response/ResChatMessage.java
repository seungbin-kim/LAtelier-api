package com.latelier.api.domain.member.packet.response;

import com.latelier.api.domain.chat.entity.ChatMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel("채팅 메세지")
public class ResChatMessage {

    @ApiModelProperty(
            value = "채팅방 ID",
            name = "chatRoomId",
            example = "3")
    private final Long chatRoomId;

    @ApiModelProperty(
            value = "메세지를 보내는 유저의 ID",
            name = "senderId",
            example = "1")
    private final Long senderId;

    @ApiModelProperty(
            value = "메세지 내용",
            name = "message",
            example = "안녕하세요.")
    private final String message;

    @ApiModelProperty(
            value = "메세지 전송시간",
            name = "createdAt")
    private final LocalDateTime createdAt;


    static public ResChatMessage of(final ChatMessage chatMessage) {

        return new ResChatMessage(
                chatMessage.getChatRoom().getId(),
                chatMessage.getMember().getId(),
                chatMessage.getMessage(),
                chatMessage.getCreatedAt());
    }

}
