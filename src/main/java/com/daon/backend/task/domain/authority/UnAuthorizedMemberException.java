package com.daon.backend.task.domain.authority;

import com.daon.backend.common.exception.AbstractException;

import java.util.Set;

public class UnAuthorizedMemberException extends AbstractException {

    public UnAuthorizedMemberException(Set<Authority> requiredAuthorities) {
        super("해당 요청에 대한 권한이 없습니다. 필요한 권한: " + requiredAuthorities.toString());
    }
}
