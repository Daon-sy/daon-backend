package com.daon.backend.task.domain.workspace.exception;

import com.daon.backend.common.exception.AbstractException;

public class NotInvitedMemberException extends AbstractException {

    public NotInvitedMemberException(Long workspaceId, String memberId) {
        super("해당 워크스페이스에 초대 받은 회원이 아닙니다. workspaceId: " + workspaceId + " memberId: " + memberId);
    }
}
