package com.daon.backend.task.controller;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.CommonResponse;
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
    public ResponseEntity<CommonResponse<Void>> notProjectParticipantExceptionHandle(NotProjectParticipantException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonResponse.createError("해당 프로젝트의 회원이 아닙니다."));
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<CommonResponse<Void>> projectNotFoundExceptionHandle(ProjectNotFoundException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonResponse.createError("존재하지 않는 프로젝트입니다."));
    }
}
