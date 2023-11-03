package com.daon.backend.image.controller;

import com.daon.backend.image.dto.UploadImageResponseDto;
import com.daon.backend.image.service.ImageFileService;
import com.daon.backend.image.service.UploadedImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController

@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageFileService imageFileService;

    @PostMapping
    public ResponseEntity<UploadImageResponseDto> uploadImage(@RequestParam("image") MultipartFile imageFile) {
        UploadedImage uploadedImage = imageFileService.upload(imageFile);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UploadImageResponseDto(uploadedImage.getUrl()));
    }
}
