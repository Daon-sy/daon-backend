package com.daon.backend.auth.domain;

import com.daon.backend.common.exception.AbstractException;

public class UnauthorizedException extends AbstractException {

    public UnauthorizedException() {
        super("인증되지 않은 사용자입니다.");
    }
}
