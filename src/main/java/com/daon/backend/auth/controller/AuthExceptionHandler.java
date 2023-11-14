package com.daon.backend.auth.controller;

import com.daon.backend.auth.domain.InvalidRefreshTokenException;
import com.daon.backend.auth.domain.UnauthenticatedMemberException;
import com.daon.backend.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> invalidRefreshTokenExceptionHandle(InvalidRefreshTokenException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.createError("로그인이 만료되었습니다."));
    }

    @ExceptionHandler(UnauthenticatedMemberException.class)
    public ResponseEntity<ErrorResponse> unauthorizedExceptionHandle(UnauthenticatedMemberException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.createError("로그인이 필요한 서비스입니다."));
    }
}
