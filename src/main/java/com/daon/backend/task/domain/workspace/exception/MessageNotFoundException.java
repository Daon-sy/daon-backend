package com.daon.backend.task.domain.workspace.exception;

import com.daon.backend.common.exception.AbstractException;

public class MessageNotFoundException extends AbstractException {

    public MessageNotFoundException(Long messageId) {
        super("해당 쪽지를 찾을 수 없습니다. messageId: " + messageId);
    }
}
