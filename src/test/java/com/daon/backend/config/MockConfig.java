package com.daon.backend.config;

import com.daon.backend.image.infrastructure.S3ImageFileService;
import com.daon.backend.mail.service.MailService;
import org.springframework.boot.test.mock.mockito.MockBean;

public abstract class MockConfig {

    @MockBean
    S3Config s3Config;

    @MockBean
    EmailConfig emailConfig;

    @MockBean
    S3ImageFileService s3ImageFileService;

    @MockBean
    MailService mailService;
}
