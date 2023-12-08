package com.daon.backend.mail.service;

import com.daon.backend.mail.dto.VerificationEmailResponseDto;

public interface MailService {

    void sendCodeToEmail(String toEmail);

    VerificationEmailResponseDto verifiedCode(String email, String code);
}
