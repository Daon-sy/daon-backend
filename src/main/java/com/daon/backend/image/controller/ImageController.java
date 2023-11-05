package com.daon.backend.image.controller;

import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.image.dto.UploadImageResponseDto;
import com.daon.backend.image.service.ImageFileService;
import com.daon.backend.image.service.UploadedImage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Image", description = "Image domain API")
@RequestMapping("/api/images")
public class ImageController {

    private final ImageFileService imageFileService;

    @Operation(summary = "이미지 업로드", description = "이미지 업로드 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이미지 업로드 성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommonResponse<UploadImageResponseDto> uploadImage(@RequestParam("image") MultipartFile imageFile) {
        UploadedImage uploadedImage = imageFileService.upload(imageFile);
        return CommonResponse.createSuccess(new UploadImageResponseDto(uploadedImage.getUrl()));
    }
}
