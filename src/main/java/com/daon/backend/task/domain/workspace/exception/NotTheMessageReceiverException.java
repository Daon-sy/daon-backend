package com.daon.backend.task.domain.workspace.exception;

import com.daon.backend.common.exception.AbstractException;

public class NotTheMessageReceiverException extends AbstractException {

    public NotTheMessageReceiverException() {
        super("요청자와 메시지 수신자가 일치하지 않습니다.");
    }
}
