package com.daon.backend.member.domain;

import com.daon.backend.common.exception.AbstractException;

public class AlreadyExistsEmailException extends AbstractException {

    public AlreadyExistsEmailException(String email) {
        super("이미 존재하는 이메일입니다. email: " + email);
    }
}
