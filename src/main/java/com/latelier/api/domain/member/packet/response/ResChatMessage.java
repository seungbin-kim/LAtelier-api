package com.latelier.api.domain.member.packet.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResChatMessage {

    private final Long chatRoomId;

    private final Long senderId;

    private final String message;

}
