package com.latelier.api.domain.member.packet.response;

import com.latelier.api.domain.chat.entity.ChatRoom;
import com.latelier.api.domain.chat.entity.ChatRoomJoin;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel("채팅방 정보")
public class ResChatRoom {

    @ApiModelProperty(
            value = "채팅방 ID",
            name = "chatRoomId",
            example = "3")
    private final Long chatRoomId;

    @ApiModelProperty(
            value = "보내는 사람 이름",
            name = "senderName",
            example = "홍길동")
    private final String senderName;

    @ApiModelProperty(
            value = "받는 사람 이름",
            name = "receiverName",
            example = "홍길순")
    private final String receiverName;


    static public ResChatRoom of(final ChatRoomJoin chatRoomJoin,
                                 final String senderName,
                                 final String receiverName) {

        return new ResChatRoom(
                chatRoomJoin.getChatRoom().getId(),
                senderName,
                receiverName);
    }


    static public ResChatRoom of(final ChatRoom chatRoom,
                                 final String senderName,
                                 final String receiverName) {

        return new ResChatRoom(
                chatRoom.getId(),
                senderName,
                receiverName);
    }

}
