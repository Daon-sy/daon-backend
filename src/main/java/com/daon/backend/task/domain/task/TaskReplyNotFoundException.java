package com.daon.backend.task.domain.task;

import com.daon.backend.common.exception.AbstractException;

public class TaskReplyNotFoundException extends AbstractException {
    public TaskReplyNotFoundException(Long taskId, Long taskReplyId) {
        super("해당 할 일에 생성된 댓글이 없습니다. taskId: " + taskId + ", taskReplyId: " + taskReplyId);
    }

    public TaskReplyNotFoundException(Long taskReplyId) {
        super("생성된 댓글이 없습니다. taskReplyId: " + taskReplyId);
    }
}
