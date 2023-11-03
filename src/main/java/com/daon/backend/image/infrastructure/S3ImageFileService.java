package com.daon.backend.image.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.daon.backend.image.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ImageFileService implements ImageFileService {

    private static final List<String> allowedContentType = List.of("image/jpg", "image/jpeg", "image/png");

    @Value("${s3.dir-name}")
    private String dirName;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    // TODO security 추가되면 userId 사용하기
    @Override
    public UploadedImage upload(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new EmptyImageException();
        }

        if (!allowedContentType.contains(multipartFile.getContentType())) {
            throw new NotAllowedContentTypeException(multipartFile.getContentType());
        }

        String filename = getFilenameToStore(multipartFile);
        String fullPath = getFullPath(filename);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            objectMetadata.setContentLength(bytes.length);
            objectMetadata.setContentType(multipartFile.getContentType());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fullPath, new ByteArrayInputStream(bytes), objectMetadata);
            amazonS3.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));

            log.debug("Upload image success!! filename: {}", filename);
        } catch (IOException e) {
            throw new ImageIOException(e);
        }

        return new UploadedImage(filename, amazonS3.getUrl(bucket, fullPath).toString());
    }

    private String getFilenameToStore(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        return generateRandomFilename() + "." + getExt(originalFilename);
    }

    private String generateRandomFilename() {
        return UUID.randomUUID().toString();
    }

    private String getExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }

    private String getFullPath(String filename) {
        return dirName + "/" + filename;
    }
}
