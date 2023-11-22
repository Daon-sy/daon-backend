package com.daon.backend.common.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private int errorCode;

    private String errorDescription;

    public static ErrorResponse createError(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .errorCode(errorCode.getCode())
                .errorDescription(errorCode.getDescription())
                .build();
    }

    public static ErrorResponse createMethodArgumentNotValidError(int errorCode, String errorDescription) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorDescription(errorDescription)
                .build();
    }
}
