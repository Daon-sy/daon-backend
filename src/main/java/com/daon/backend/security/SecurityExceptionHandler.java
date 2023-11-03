package com.daon.backend.security;

import com.daon.backend.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(MemberNotAuthenticatedException.class)
    public ResponseEntity<ApiResponse<Void>> memberNotAuthenticatedExceptionHandle(MemberNotAuthenticatedException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.createError("인증되지 않은 사용자의 요청입니다."));
    }
}