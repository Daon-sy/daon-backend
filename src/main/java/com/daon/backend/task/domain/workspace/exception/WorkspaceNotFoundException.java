package com.daon.backend.task.domain.workspace.exception;

import com.daon.backend.common.exception.AbstractException;

public class WorkspaceNotFoundException extends AbstractException {

    public WorkspaceNotFoundException(Long workspaceId) {
        super("존재하지 않는 워크스페이스입니다. 요청된 워크스페이스 식별값: " + workspaceId);
    }
}
