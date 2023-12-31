package com.daon.backend.task.domain.task;

import com.daon.backend.common.exception.AbstractException;

public class TaskNotFoundException extends AbstractException {

    public TaskNotFoundException(Long projectId, Long taskId) {
        super("해당 프로젝트에 생성된 할 일이 없습니다. projectId: " + projectId + ", taskId: " + taskId);
    }

    public TaskNotFoundException(Long taskId) {
        super("생성된 할 일이 없습니다. taskId: " + taskId);
    }
}
