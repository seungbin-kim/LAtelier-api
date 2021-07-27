package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.ErrorCode;
import com.latelier.api.global.error.exception.NotFoundException;

public class ChatRoomNotFound extends NotFoundException {

    public ChatRoomNotFound(final String value) {

        super(value, ErrorCode.CHAT_ROOM_NOT_FOUND);
    }
}
