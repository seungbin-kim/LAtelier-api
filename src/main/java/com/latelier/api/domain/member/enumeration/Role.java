package com.latelier.api.domain.member.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_USER("user"),
    ROLE_INSTRUCTOR("instructor"),
    ROLE_ADMIN("admin");

    final String roleName;

}
