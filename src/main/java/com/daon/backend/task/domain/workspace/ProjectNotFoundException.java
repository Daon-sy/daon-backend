package com.daon.backend.task.domain.workspace;

import com.daon.backend.common.exception.AbstractException;

public class ProjectNotFoundException extends AbstractException {

    public ProjectNotFoundException(Long projectId) {
        super("해당 프로젝트는 존재하지 않습니다. projectId: " + projectId);
    }
}
