package com.daon.backend.task.domain.project;

import com.daon.backend.common.exception.AbstractException;

public class AlreadyInvitedWorkspaceParticipantException extends AbstractException {

    public AlreadyInvitedWorkspaceParticipantException(Long invitedWorkspaceParticipantId) {
        super("이미 참여 중인 워크스페이스 참여자입니다. invitedWorkspaceParticipantId: " + invitedWorkspaceParticipantId);
    }
}
