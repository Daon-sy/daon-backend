package com.daon.backend.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    MEMBER("ROLE_MEMBER"),
    ADMIN("ROLE_ADMIN"),
    ;

    private final String value;
}
