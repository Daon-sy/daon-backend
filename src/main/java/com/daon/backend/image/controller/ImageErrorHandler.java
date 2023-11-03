package com.daon.backend.image.controller;

import com.daon.backend.common.exception.DomainSpecificAdvice;
import com.daon.backend.common.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<Void>> emptyImageExceptionHandle(EmptyImageException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.createError("빈 이미지 파일입니다."));
    }

    @ExceptionHandler(NotAllowedContentTypeException.class)
    public ResponseEntity<ApiResponse<Void>> notAllowedContentTypeExceptionHandle(NotAllowedContentTypeException e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.createError("이미지는 .jpg, .jpeg, .png 형식이어야 합니다."));
    }

    @ExceptionHandler(ImageIOException.class)
    public ResponseEntity<ApiResponse<Void>> imageIOExceptionHandle(ImageIOException e) {
        log.error("이미지 입출력 오류 발생...", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.createError("이미지 업로드 중 오류 발생"));
    }
}
