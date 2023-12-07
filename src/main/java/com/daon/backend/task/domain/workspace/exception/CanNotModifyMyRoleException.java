package com.daon.backend.task.domain.workspace.exception;

import com.daon.backend.common.exception.AbstractException;

public class CanNotModifyMyRoleException extends AbstractException {

    public CanNotModifyMyRoleException(String memberId) {
        super("본인의 역할은 변경할 수 없습니다. memberId: " + memberId);
    }
}
