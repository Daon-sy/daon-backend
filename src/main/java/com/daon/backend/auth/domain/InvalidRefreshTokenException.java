package com.daon.backend.auth.domain;

import com.daon.backend.common.exception.AbstractException;

public class InvalidRefreshTokenException extends AbstractException {

    public InvalidRefreshTokenException() {
        super("유효하지 않은 리프레시 토큰입니다.");
    }
}
