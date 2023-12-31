package com.daon.backend.security;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.error.ErrorCode;
import com.daon.backend.common.response.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(MemberNotAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> memberNotAuthenticatedExceptionHandle(MemberNotAuthenticatedException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.createError(ErrorCode.MEMBER_NOT_AUTHENTICATED));
    }
}
