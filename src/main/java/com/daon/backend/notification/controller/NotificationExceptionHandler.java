package com.daon.backend.notification.controller;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.error.ErrorCode;
import com.daon.backend.common.response.error.ErrorResponse;
import com.daon.backend.notification.domain.TypeNotSpecifiedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class NotificationExceptionHandler {

    @ExceptionHandler(TypeNotSpecifiedException.class)
    public ResponseEntity<ErrorResponse> typeNotSpecifiedExceptionHandle(TypeNotSpecifiedException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.TYPE_NOT_SPECIFIC));
    }
}
