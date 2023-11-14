package com.daon.backend.common.exception;

import com.daon.backend.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        log.error("데이터 검증 오류");

        BindingResult bindingResult = e.getBindingResult();
        // 필드 오류 메시지들 하나로 묶기
        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(this::parseFieldErrorMessage)
                .collect(Collectors.joining(","));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(errorMessage));
    }

    private String parseFieldErrorMessage(FieldError fieldError) {
        String field = fieldError.getField();
        String fieldErrorDefaultMessage = fieldError.getDefaultMessage();
        return field + ":" + fieldErrorDefaultMessage;
    }

    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<ErrorResponse> abstractExceptionHandle(AbstractException e) {
        log.error("{}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.createError("서버 오류입니다. 문의 바랍니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> allExceptionHandle(Exception e) {
        log.error("{}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.createError("서버 오류입니다. 문의 바랍니다."));
    }
}
