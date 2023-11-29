package com.daon.backend.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/emails/verification")
@RestController
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public void sendVerificationEmail(@RequestBody SendVerificationEmailRequestDto requestDto) {
        mailService.sendCodeToEmail(requestDto.getEmail());
    }

    @PostMapping("/check")
    public VerificationEmailResponseDto verificationEmail(@RequestBody VerificationEmailCodeRequestDto requestDto) {
        return mailService.verifiedCode(requestDto.getEmail(), requestDto.getCode());
    }
}
