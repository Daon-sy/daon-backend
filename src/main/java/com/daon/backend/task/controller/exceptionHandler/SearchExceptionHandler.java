package com.daon.backend.task.controller.exceptionHandler;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.error.ErrorCode;
import com.daon.backend.common.error.ErrorResponse;
import com.daon.backend.task.service.InvalidTargetException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class SearchExceptionHandler {

    @ExceptionHandler(InvalidTargetException.class)
    public ResponseEntity<ErrorResponse> invalidTargetExceptionHandle(InvalidTargetException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.INVALID_TARGET));
    }
}
