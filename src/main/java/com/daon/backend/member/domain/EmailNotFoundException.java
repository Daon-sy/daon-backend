package com.daon.backend.member.domain;

import com.daon.backend.common.exception.AbstractException;

public class EmailNotFoundException extends AbstractException {
    public EmailNotFoundException(Long memberEmailId) {
        super("이메일을 찾을 수 없습니다. requestedMemberEmailId: " + memberEmailId);
    }
}
