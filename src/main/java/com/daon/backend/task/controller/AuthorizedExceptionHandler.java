package com.daon.backend.task.controller;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.ErrorResponse;
import com.daon.backend.task.domain.authority.UnAuthorizedMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class AuthorizedExceptionHandler {

    @ExceptionHandler(UnAuthorizedMemberException.class)
    public ResponseEntity<ErrorResponse> unAuthorizedMemberExceptionHandle(UnAuthorizedMemberException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.createError("해당 요청에 대한 권한이 없습니다."));
    }
}
