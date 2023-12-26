package com.daon.backend.task.domain.workspace.exception;

import com.daon.backend.common.exception.AbstractException;

public class CanNotSendMessageToMeException extends AbstractException {

    public CanNotSendMessageToMeException(Long workspaceParticipantId) {
        super("본인에게는 쪽지를 보낼 수 없습니다. workspaceParticipantId: " + workspaceParticipantId);
    }
}
