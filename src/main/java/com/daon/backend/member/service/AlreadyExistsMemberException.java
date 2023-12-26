package com.daon.backend.member.service;

import com.daon.backend.common.exception.AbstractException;

public class AlreadyExistsMemberException extends AbstractException {

    public AlreadyExistsMemberException(String username) {
        super("이미 존재하는 회원입니다. username : " + username);
    }
}
