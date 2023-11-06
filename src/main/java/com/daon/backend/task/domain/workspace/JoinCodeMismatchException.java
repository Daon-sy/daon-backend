package com.daon.backend.task.domain.workspace;

import com.daon.backend.common.exception.AbstractException;

public class JoinCodeMismatchException extends AbstractException {

    public JoinCodeMismatchException(String joinCode) {
        super("초대 코드가 일치하지 않습니다. 요청 받은 초대 코드: " + joinCode);
    }
}
