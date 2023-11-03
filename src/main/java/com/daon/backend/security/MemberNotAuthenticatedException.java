package com.daon.backend.security;

import com.daon.backend.common.exception.AbstractException;

public class MemberNotAuthenticatedException extends AbstractException {

    public MemberNotAuthenticatedException() {
        super("인증되지 않은 사용자의 요청이기 때문에 인증 객체가 존재하지 않습니다.");
    }
}
