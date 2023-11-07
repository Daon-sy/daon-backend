package com.daon.backend.task.controller;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.task.domain.workspace.JoinCodeMismatchException;
import com.daon.backend.task.domain.workspace.NotWorkspaceParticipantException;
import com.daon.backend.task.domain.workspace.WorkspaceNotFoundException;
import com.daon.backend.task.service.SameMemberExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class WorkspaceExceptionHandler {

    @ExceptionHandler(WorkspaceNotFoundException.class)
    public ResponseEntity<CommonResponse<Void>> workspaceNotFoundExceptionHandle(WorkspaceNotFoundException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonResponse.createError("요청한 워크스페이스는 존재하지 않습니다."));
    }

    @ExceptionHandler(NotWorkspaceParticipantException.class)
    public ResponseEntity<CommonResponse<Void>> notWorkspaceParticipantExceptionHandle(NotWorkspaceParticipantException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(CommonResponse.createError("해당 워크스페이스에 접근 권한이 없습니다."));
    }

    @ExceptionHandler(JoinCodeMismatchException.class)
    public ResponseEntity<CommonResponse<Void>> joinCodeMismatchExceptionHandle(JoinCodeMismatchException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.createError("초대 코드가 일치하지 않습니다."));
    }

    @ExceptionHandler(SameMemberExistsException.class)
    public ResponseEntity<CommonResponse<Void>> sameMemberExistsExceptionHandle(SameMemberExistsException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.createError("이미 참여중인 구성원입니다."));
    }
}
