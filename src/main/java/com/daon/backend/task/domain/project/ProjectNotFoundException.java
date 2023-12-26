package com.daon.backend.task.domain.project;

import com.daon.backend.common.exception.AbstractException;

public class ProjectNotFoundException extends AbstractException {

    public ProjectNotFoundException(Long projectId) {
        super("존재하지 않는 프로젝트입니다. 요청된 프로젝트 식별값: " + projectId);
    }
}
