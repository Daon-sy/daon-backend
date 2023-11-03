package com.daon.backend.member.controller;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.ErrorResponse;
import com.daon.backend.member.service.AlreadyExistsMemberException;
import com.daon.backend.member.service.NotFoundEmailException;
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
                .body(new ErrorResponse("이미 존재하는 회원입니다."));
    }

    @ExceptionHandler(NotFoundEmailException.class)
    public ResponseEntity<ErrorResponse> notFoundEmailExceptionHandle(NotFoundEmailException e) {
        log.error("{}", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("존재하지 않는 이메일입니다."));
    }
}
