package com.daon.backend.task.controller;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.ErrorResponse;
import com.daon.backend.task.domain.project.TaskNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class TaskExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> taskNotFoundExceptionHandle(TaskNotFoundException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.createError("생성된 할 일이 없습니다."));
    }
}
