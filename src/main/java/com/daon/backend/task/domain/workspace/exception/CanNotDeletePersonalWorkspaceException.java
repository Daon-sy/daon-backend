package com.daon.backend.task.domain.workspace.exception;

import com.daon.backend.common.exception.AbstractException;

public class CanNotDeletePersonalWorkspaceException extends AbstractException {

    public CanNotDeletePersonalWorkspaceException(Long workspaceId) {
        super("개인 워크스페이스는 삭제할 수 없습니다. workspaceId: " + workspaceId);
    }
}
