package com.daon.backend.auth.domain;

import com.daon.backend.common.exception.AbstractException;

public class UnauthenticatedMemberException extends AbstractException {

    public UnauthenticatedMemberException() {
        super("인증되지 않은 사용자입니다.");
    }
}
