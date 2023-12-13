package com.daon.backend.mail.service;

public interface MailService {

    void sendCodeToEmail(String toEmail);

    void verifiedCode(String email, String code);
}
