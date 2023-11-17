package com.daon.backend.member.domain;

import com.daon.backend.common.exception.AbstractException;

public class EmailNotFoundException extends AbstractException {

    public EmailNotFoundException(String email) {
        super("이메일을 찾을 수 없습니다. requestedEmail: " + email);
    }

    public EmailNotFoundException(Long memberEmailId) {
        super("이메일을 찾을 수 없습니다. requestedMemberEmailId: " + memberEmailId);
    }
}
