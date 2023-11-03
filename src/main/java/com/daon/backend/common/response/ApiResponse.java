package com.daon.backend.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private T data;
    private String message;

    public static <T> ApiResponse<T> createSuccess(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> createSuccess(T data, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> createError(String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> createError(T data, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .message(message)
                .build();
    }
}
