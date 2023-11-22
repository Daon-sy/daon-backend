package com.daon.backend.task.domain.workspace;

import com.daon.backend.common.exception.AbstractException;

public class CanNotPersonalWorkspaceException extends AbstractException {

    public CanNotPersonalWorkspaceException(Long workspaceId) {
        super("개인 워크스페이스는 삭제할 수 없습니다. workspaceId: " + workspaceId);
    }
}
