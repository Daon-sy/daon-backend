package com.daon.backend.task.domain.project;

import com.daon.backend.common.exception.AbstractException;

public class CanNotDeleteBoardException extends AbstractException {

    public CanNotDeleteBoardException() {
        super("보드가 1개 남았기 때문에 삭제할 수 없습니다.");
    }
}
