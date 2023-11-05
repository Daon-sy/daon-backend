package com.daon.backend.member.controller;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.member.domain.PasswordMismatchException;
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
    public ResponseEntity<CommonResponse<Void>> alreadyExistsMemberExceptionHandle(AlreadyExistsMemberException e) {
        log.error("{}", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.createError("이미 존재하는 회원입니다."));
    }

    @ExceptionHandler(NotFoundEmailException.class)
    public ResponseEntity<CommonResponse<Void>> notFoundEmailExceptionHandle(NotFoundEmailException e) {
        log.error("{}", e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse.createError("존재하지 않는 이메일입니다."));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<CommonResponse<Void>> passwordMismatchExceptionHandle(PasswordMismatchException e) {
        log.error("{}", e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse.createError("비밀번호가 일치하지 않습니다."));
    }
}
