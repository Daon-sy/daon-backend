package com.daon.backend.task.controller;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.task.domain.project.SameBoardExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class BoardExceptionHandler {

    @ExceptionHandler(SameBoardExistsException.class)
    public ResponseEntity<CommonResponse<Void>> sameBoardExistsException(SameBoardExistsException e) {
        log.info("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.createError("동일한 이름의 보드가 존재합니다."));
    }
}
