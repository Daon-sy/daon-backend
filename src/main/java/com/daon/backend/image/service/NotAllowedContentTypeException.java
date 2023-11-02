package com.daon.backend.image.service;

import com.daon.backend.common.exception.AbstractException;

public class NotAllowedContentTypeException extends AbstractException {

    public NotAllowedContentTypeException(String originalContentType) {
        super("허용되지 않은 content-type 입니다. 요청된 content-type: " + originalContentType);
    }
}
