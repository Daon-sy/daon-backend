package com.daon.backend.task.domain.workspace.exception;

import com.daon.backend.common.exception.AbstractException;

public class CanNotInvitePersonalWorkspaceException extends AbstractException {

    public CanNotInvitePersonalWorkspaceException(Long workspaceId) {
        super("개인 워크스페이스에는 초대할 수 없습니다. workspaceId: " + workspaceId);
    }
}
