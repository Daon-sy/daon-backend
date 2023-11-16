package com.daon.backend.task.controller;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.ErrorCode;
import com.daon.backend.common.response.ErrorResponse;
import com.daon.backend.task.domain.workspace.NotInvitedMemberException;
import com.daon.backend.task.domain.workspace.NotWorkspaceParticipantException;
import com.daon.backend.task.domain.workspace.SameMemberExistsException;
import com.daon.backend.task.domain.workspace.WorkspaceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class WorkspaceExceptionHandler {

    @ExceptionHandler(WorkspaceNotFoundException.class)
    public ResponseEntity<ErrorResponse> workspaceNotFoundExceptionHandle(WorkspaceNotFoundException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.createError(ErrorCode.WORKSPACE_NOT_FOUND));
    }

    @ExceptionHandler(NotWorkspaceParticipantException.class)
    public ResponseEntity<ErrorResponse> notWorkspaceParticipantExceptionHandle(NotWorkspaceParticipantException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.createError(ErrorCode.NOT_WORKSPACE_PARTICIPANT));
    }

    @ExceptionHandler(SameMemberExistsException.class)
    public ResponseEntity<ErrorResponse> sameMemberExistsExceptionHandle(SameMemberExistsException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.SAME_MEMBER_EXISTS_IN_WORKSPACE));
    }

    @ExceptionHandler(NotInvitedMemberException.class)
    public ResponseEntity<ErrorResponse> notInvitedMemberExceptionHandle(NotInvitedMemberException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.NOT_INVITED_MEMBER));
    }
}
