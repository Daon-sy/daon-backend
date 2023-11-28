package com.daon.backend.task.controller.exceptionHandler;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.error.ErrorCode;
import com.daon.backend.common.response.error.ErrorResponse;
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
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.createError(ErrorCode.UNAUTHORIZED_MEMBER));
    }
}
