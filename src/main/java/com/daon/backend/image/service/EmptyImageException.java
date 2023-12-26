package com.daon.backend.image.service;

import com.daon.backend.common.exception.AbstractException;

public class EmptyImageException extends AbstractException {

    public EmptyImageException() {
        super("빈 이미지 파일입니다.");
    }
}
