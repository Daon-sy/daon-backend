package com.daon.backend.notification.domain;

import com.daon.backend.common.exception.AbstractException;

public class TypeNotSpecifiedException extends AbstractException {

    public TypeNotSpecifiedException() {
        super("SSE 연결 요청(할 일 목록 조회)에 타입이 지정되지 않았습니다.");
    }
}
