package com.daon.backend.task.domain.task;

import com.daon.backend.common.exception.AbstractException;

public class ReplyNotFoundException extends AbstractException {
    public ReplyNotFoundException(Long taskId, Long replyId) {
        super("해당 할 일에 생성된 댓글이 없습니다. taskId: " + taskId + ", replyId: " + replyId);
    }

    public ReplyNotFoundException(Long replyId) {
        super("생성된 댓글이 없습니다. replyId: " + replyId);
    }
}
