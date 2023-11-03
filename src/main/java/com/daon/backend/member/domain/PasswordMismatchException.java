package com.daon.backend.member.domain;

import com.daon.backend.common.exception.AbstractException;

public class PasswordMismatchException extends AbstractException {

    public PasswordMismatchException(String memberId) {
        super("비밀번호 불일치 오류!! memberId: " + memberId);
    }
}
