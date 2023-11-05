package com.daon.backend.image.controller;

import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.image.dto.UploadImageResponseDto;
import com.daon.backend.image.service.ImageFileService;
import com.daon.backend.image.service.UploadedImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController

@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageFileService imageFileService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommonResponse<UploadImageResponseDto> uploadImage(@RequestParam("image") MultipartFile imageFile) {
        UploadedImage uploadedImage = imageFileService.upload(imageFile);
        return CommonResponse.createSuccess(new UploadImageResponseDto(uploadedImage.getUrl()));
    }
}
