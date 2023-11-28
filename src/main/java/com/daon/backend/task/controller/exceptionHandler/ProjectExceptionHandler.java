package com.daon.backend.task.controller.exceptionHandler;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.error.ErrorCode;
import com.daon.backend.common.response.error.ErrorResponse;
import com.daon.backend.task.domain.project.NotProjectParticipantException;
import com.daon.backend.task.domain.project.ProjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class ProjectExceptionHandler {

    @ExceptionHandler(NotProjectParticipantException.class)
    public ResponseEntity<ErrorResponse> notProjectParticipantExceptionHandle(NotProjectParticipantException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.createError(ErrorCode.NOT_PROJECT_PARTICIPANT));
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> projectNotFoundExceptionHandle(ProjectNotFoundException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.createError(ErrorCode.PROJECT_NOT_FOUND));
    }
}
