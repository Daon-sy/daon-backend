package com.daon.backend.image.service;

import com.daon.backend.image.dto.UploadedImage;
import org.springframework.web.multipart.MultipartFile;

public interface ImageFileService {

    UploadedImage upload(MultipartFile multipartFile);
}
