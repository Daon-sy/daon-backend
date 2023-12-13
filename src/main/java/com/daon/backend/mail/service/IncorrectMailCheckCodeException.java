package com.daon.backend.mail.service;

import com.daon.backend.common.exception.AbstractException;

public class IncorrectMailCheckCodeException extends AbstractException {

    public IncorrectMailCheckCodeException(String email, String code) {
        super("인증 번호가 일치하지 않습니다. 이메일: " + email + " 요청 받은 코드: " + code);
    }
}
