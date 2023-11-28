package com.daon.backend.mail;

import com.daon.backend.common.exception.AbstractException;

public class UnableToSendEmailException extends AbstractException {

    public UnableToSendEmailException() {
        super("메일을 전송하는데 실패했습니다.");
    }
}
