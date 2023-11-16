package com.daon.backend.member.controller;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.ErrorCode;
import com.daon.backend.common.response.ErrorResponse;
import com.daon.backend.member.domain.EmailNotFoundException;
import com.daon.backend.member.domain.MemberNotFoundException;
import com.daon.backend.member.domain.PasswordMismatchException;
import com.daon.backend.member.service.AlreadyExistsMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class MemberErrorHandler {

    @ExceptionHandler(AlreadyExistsMemberException.class)
    public ResponseEntity<ErrorResponse> alreadyExistsMemberExceptionHandle(AlreadyExistsMemberException e) {
        log.error("{}", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.ALREADY_EXISTS_MEMBER));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> memberNotFoundExceptionHandle(MemberNotFoundException e) {
        log.error("{}", e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.createError(ErrorCode.MEMBER_NOT_FOUND));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> passwordMismatchExceptionHandle(PasswordMismatchException e) {
        log.error("{}", e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.createError(ErrorCode.PASSWORD_MISMATCH));
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> emailNotFoundExceptionHandle(EmailNotFoundException e) {
        log.error("{}", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.createError(ErrorCode.EMAIL_NOT_FOUND));
    }
}
