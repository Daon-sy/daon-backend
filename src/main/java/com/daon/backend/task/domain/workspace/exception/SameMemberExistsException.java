package com.daon.backend.task.domain.workspace.exception;

import com.daon.backend.common.exception.AbstractException;

public class SameMemberExistsException extends AbstractException {

    public SameMemberExistsException(String memberId) {
        super("이미 참여중인 구성원입니다. memberId: " + memberId);
    }
}
