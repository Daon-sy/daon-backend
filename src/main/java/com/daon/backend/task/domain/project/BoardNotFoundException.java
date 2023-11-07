package com.daon.backend.task.domain.project;

import com.daon.backend.common.exception.AbstractException;

public class BoardNotFoundException extends AbstractException {

    public BoardNotFoundException(Long workspaceId, Long projectId) {
        super("해당 워크스페이스 내의 프로젝트에 유효한 보드가 없습니다. workspaceId: " + workspaceId + " projectId: " + projectId);
    }
}
