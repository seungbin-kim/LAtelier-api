package com.latelier.api.domain.member.packet.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResChatRoom {

    private final Long chatRoomId;

    private final String senderName;

    private final String receiverName;

}
