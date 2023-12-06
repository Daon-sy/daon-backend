package com.daon.backend.task.controller.exceptionHandler;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.error.ErrorCode;
import com.daon.backend.common.response.error.ErrorResponse;
import com.daon.backend.task.domain.workspace.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class WorkspaceExceptionHandler {

    @ExceptionHandler(WorkspaceNotFoundException.class)
    public ResponseEntity<ErrorResponse> workspaceNotFoundExceptionHandle(WorkspaceNotFoundException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.createError(ErrorCode.WORKSPACE_NOT_FOUND));
    }

    @ExceptionHandler(NotWorkspaceParticipantException.class)
    public ResponseEntity<ErrorResponse> notWorkspaceParticipantExceptionHandle(NotWorkspaceParticipantException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.createError(ErrorCode.NOT_WORKSPACE_PARTICIPANT));
    }

    @ExceptionHandler(SameMemberExistsException.class)
    public ResponseEntity<ErrorResponse> sameMemberExistsExceptionHandle(SameMemberExistsException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.SAME_MEMBER_EXISTS_IN_WORKSPACE));
    }

    @ExceptionHandler(NotInvitedMemberException.class)
    public ResponseEntity<ErrorResponse> notInvitedMemberExceptionHandle(NotInvitedMemberException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.NOT_INVITED_MEMBER));
    }

    @ExceptionHandler(CanNotDeletePersonalWorkspaceException.class)
    public ResponseEntity<ErrorResponse> canNotDeletePersonalWorkspaceExceptionHandle(CanNotDeletePersonalWorkspaceException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.CAN_NOT_DELETE_PERSONAL_WORKSPACE));
    }

    @ExceptionHandler(CanNotModifyMyRoleException.class)
    public ResponseEntity<ErrorResponse> canNotModifyMyRoleExceptionHandle(CanNotModifyMyRoleException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.CAN_NOT_MODIFY_MY_ROLE));
    }

    @ExceptionHandler(CanNotInvitePersonalWorkspaceException.class)
    public ResponseEntity<ErrorResponse> canNotInvitePersonalWorkspaceExceptionHandle(CanNotInvitePersonalWorkspaceException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.CAN_NOT_INVITE_PERSONAL_WORKSPACE));
    }

    @ExceptionHandler(AlreadyInvitedMemberException.class)
    public ResponseEntity<ErrorResponse> alreadyInvitedMemberExceptionHandle(AlreadyInvitedMemberException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.ALREADY_INVITED_MEMBER));
    }

    @ExceptionHandler(CanNotSendMessageToMeException.class)
    public ResponseEntity<ErrorResponse> canNotSendMessageToMeException(CanNotSendMessageToMeException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.CAN_NOT_SEND_MESSAGE_TO_ME));
    }
}
