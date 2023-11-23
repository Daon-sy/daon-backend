package com.daon.backend.image.controller;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.error.ErrorCode;
import com.daon.backend.common.response.error.ErrorResponse;
import com.daon.backend.image.service.EmptyImageException;
import com.daon.backend.image.service.ImageIOException;
import com.daon.backend.image.service.NotAllowedContentTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@DomainSpecificAdvice
public class ImageErrorHandler {

    @ExceptionHandler(EmptyImageException.class)
    public ResponseEntity<ErrorResponse> emptyImageExceptionHandle(EmptyImageException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.EMPTY_IMAGE));
    }

    @ExceptionHandler(NotAllowedContentTypeException.class)
    public ResponseEntity<ErrorResponse> notAllowedContentTypeExceptionHandle(NotAllowedContentTypeException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createError(ErrorCode.NOT_ALLOWED_CONTENT_TYPE));
    }

    @ExceptionHandler(ImageIOException.class)
    public ResponseEntity<ErrorResponse> imageIOExceptionHandle(ImageIOException e) {
        log.error("이미지 입출력 오류 발생...", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.createError(ErrorCode.IMAGE_IOEXCEPTION));
    }
}
