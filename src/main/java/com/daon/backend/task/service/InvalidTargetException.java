package com.daon.backend.task.service;

import com.daon.backend.common.exception.AbstractException;

public class InvalidTargetException extends AbstractException {

    public InvalidTargetException(String target) {
        super("유효하지 않은 분류 값입니다. target: " + target);
    }
}
