package com.daon.backend.task.domain.workspace;

import com.daon.backend.common.exception.AbstractException;

public class NotWorkspaceParticipantException extends AbstractException {

    public NotWorkspaceParticipantException(String memberId, Long workspaceId) {
        super("해당 워크스페이스의 회원이 아닙니다. memberId: " + memberId + ", workspaceId: " + workspaceId);
    }
}
