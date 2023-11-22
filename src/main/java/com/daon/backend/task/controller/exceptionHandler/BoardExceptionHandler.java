package com.daon.backend.task.controller.exceptionHandler;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.error.ErrorCode;
import com.daon.backend.common.error.ErrorResponse;
import com.daon.backend.task.domain.project.BoardNotFoundException;
import com.daon.backend.task.domain.project.CanNotDeleteBoardException;
import com.daon.backend.task.domain.project.SameBoardExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class BoardExceptionHandler {

    @ExceptionHandler(SameBoardExistsException.class)
    public ResponseEntity<ErrorResponse> sameBoardExistsExceptionHandle(SameBoardExistsException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.SAME_BOARD_EXISTS));
    }

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ErrorResponse> boardNotFoundExceptionHandle(BoardNotFoundException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.createError(ErrorCode.BOARD_NOT_FOUND));
    }

    @ExceptionHandler(CanNotDeleteBoardException.class)
    public ResponseEntity<ErrorResponse> canNotDeleteBoardException(CanNotDeleteBoardException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.CAN_NOT_DELETE_BOARD));
    }
}
