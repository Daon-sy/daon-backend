package com.daon.backend.mail;

import com.daon.backend.common.exception.AbstractException;

public class EmailVerificationTimeExpireException extends AbstractException {

    public EmailVerificationTimeExpireException(String email) {
        super("이메일 인증 시간이 지났습니다. email: " + email);
    }
}
