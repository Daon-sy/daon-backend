package com.daon.backend.member.service;

import com.daon.backend.common.exception.AbstractException;

public class NotFoundEmailException extends AbstractException {

    public NotFoundEmailException(String email) {
        super("존재하지 않는 이메일입니다. 요청 받은 이메일 : " + email);
    }
}
