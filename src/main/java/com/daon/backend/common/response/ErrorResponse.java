package com.daon.backend.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private String message;

    public static ErrorResponse createError(String message) {
        return ErrorResponse.builder()
                .message(message)
                .build();
    }
}
