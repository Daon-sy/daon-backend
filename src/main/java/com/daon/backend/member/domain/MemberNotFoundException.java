package com.daon.backend.member.domain;

import com.daon.backend.common.exception.AbstractException;

public class MemberNotFoundException extends AbstractException {

    private MemberNotFoundException(String message) {
        super(message);
    }

    public static MemberNotFoundException byUsername(String username) {
        return new MemberNotFoundException("존재하지 않는 회원입니다. username: " + username);
    }

    public static MemberNotFoundException byMemberId(String memberId) {
        return new MemberNotFoundException("존재하지 않는 회원입니다. 요청한 아이디: " + memberId);
    }
}
