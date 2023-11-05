package com.daon.backend.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonResponse<T> {

    private T data;
    private String message;

    public static <T> CommonResponse<T> createSuccess(T data) {
        return CommonResponse.<T>builder()
                .data(data)
                .build();
    }

    public static <T> CommonResponse<T> createSuccess(T data, String message) {
        return CommonResponse.<T>builder()
                .data(data)
                .message(message)
                .build();
    }

    public static <T> CommonResponse<T> createError(String message) {
        return CommonResponse.<T>builder()
                .message(message)
                .build();
    }

    public static <T> CommonResponse<T> createError(T data, String message) {
        return CommonResponse.<T>builder()
                .data(data)
                .message(message)
                .build();
    }
}
