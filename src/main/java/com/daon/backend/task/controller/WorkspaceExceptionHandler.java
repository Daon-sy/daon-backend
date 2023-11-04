package com.daon.backend.task.controller;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.ApiResponse;
import com.daon.backend.task.domain.workspace.NotWorkspaceParticipantException;
import com.daon.backend.task.domain.workspace.WorkspaceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class WorkspaceExceptionHandler {

    @ExceptionHandler(WorkspaceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> workspaceNotFoundExceptionHandle(WorkspaceNotFoundException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.createError("요청한 워크스페이스는 존재하지 않습니다."));
    }

    @ExceptionHandler(NotWorkspaceParticipantException.class)
    public ResponseEntity<ApiResponse<Void>> notWorkspaceParticipantExceptionHandle(NotWorkspaceParticipantException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.createError("해당 워크스페이스에 접근 권한이 없습니다."));
    }
}