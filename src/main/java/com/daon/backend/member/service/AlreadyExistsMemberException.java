package com.daon.backend.member.service;

import com.daon.backend.common.exception.AbstractException;

public class AlreadyExistsMemberException extends AbstractException {

    public AlreadyExistsMemberException(String memberEmail) {
        super("이미 존재하는 회원입니다. 요청 받은 이메일 : " + memberEmail);
    }
}
