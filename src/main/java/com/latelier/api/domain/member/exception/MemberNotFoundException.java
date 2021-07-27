package com.latelier.api.domain.member.exception;

import com.latelier.api.global.error.exception.ErrorCode;
import com.latelier.api.global.error.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException(final String value) {

        super(value, ErrorCode.MEMBER_NOT_FOUND);
    }

}
