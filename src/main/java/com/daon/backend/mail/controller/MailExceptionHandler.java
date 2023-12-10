package com.daon.backend.mail.controller;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.error.ErrorCode;
import com.daon.backend.common.response.error.ErrorResponse;
import com.daon.backend.mail.service.EmailVerificationTimeExpireException;
import com.daon.backend.mail.service.UnableToSendEmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class MailExceptionHandler {

    @ExceptionHandler(UnableToSendEmailException.class)
    public ResponseEntity<ErrorResponse> unableToSendEmailExceptionHandle(UnableToSendEmailException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.createError(ErrorCode.UNABLE_TO_SEND_EMAIL));
    }

    @ExceptionHandler(EmailVerificationTimeExpireException.class)
    public ResponseEntity<ErrorResponse> mmailVerificationTimeExpireExceptionHandle(EmailVerificationTimeExpireException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.EMAIL_VERIFICATION_TIME_EXPIRE));
    }
}
