package com.daon.backend.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/emails/verification")
@RestController
public class MailController {

    private final MailService mailService;

    @PostMapping
    public void sendMessage(@RequestParam String email) {
        mailService.sendCodeToEmail(email);
    }

    @GetMapping
    public VerificationEmailResponseDto verificationEmail(@RequestParam String email,
                                                          @RequestParam String code) {
        return mailService.verifiedCode(email, code);
    }
}
