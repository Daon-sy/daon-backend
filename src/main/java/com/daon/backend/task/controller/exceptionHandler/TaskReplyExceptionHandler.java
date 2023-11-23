package com.daon.backend.task.controller.exceptionHandler;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.error.ErrorCode;
import com.daon.backend.common.response.error.ErrorResponse;
import com.daon.backend.task.domain.task.TaskReplyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class TaskReplyExceptionHandler {

    @ExceptionHandler(TaskReplyNotFoundException.class)
    public ResponseEntity<ErrorResponse> taskReplyNotFoundExceptionHandle(TaskReplyNotFoundException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.createError(ErrorCode.REPLY_NOT_FOUND));
    }

}
