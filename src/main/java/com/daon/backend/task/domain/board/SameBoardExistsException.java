package com.daon.backend.task.domain.board;

import com.daon.backend.common.exception.AbstractException;

public class SameBoardExistsException extends AbstractException {

    public SameBoardExistsException(String title) {
        super("동일한 이름의 보드가 이미 존재합니다. title: " + title);
    }
}
