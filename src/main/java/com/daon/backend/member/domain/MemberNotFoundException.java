package com.daon.backend.member.domain;

import com.daon.backend.common.exception.AbstractException;

import java.util.UUID;

public class MemberNotFoundException extends AbstractException {

    private MemberNotFoundException(String message) {
        super(message);
    }

    public static MemberNotFoundException byEmail(String email) {
        return new MemberNotFoundException("존재하지 않는 회원입니다. 요청한 이메일: " + email);
    }

    public static MemberNotFoundException byMemberId(UUID memberId) {
        return new MemberNotFoundException("존재하지 않는 회원입니다. 요청한 아이디: " + memberId);
    }

}
