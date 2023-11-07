package com.daon.backend.member.domain;

import com.daon.backend.common.exception.AbstractException;

public class MemberNotFoundException extends AbstractException {

    private MemberNotFoundException(String message) {
        super(message);
    }

    public static MemberNotFoundException byEmail(String email) {
        return new MemberNotFoundException("존재하지 않는 회원입니다. 요청한 이메일: " + email);
    }

}
