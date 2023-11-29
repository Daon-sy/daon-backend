package com.daon.backend.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/emails/verification")
@RestController
public class MailController {

    private final MailService mailService;

    @PostMapping
    public void sendVerificationEmail(@RequestBody SendVerificationEmailRequestDto requestDto) {
        mailService.sendCodeToEmail(requestDto.getEmail());
    }

    @GetMapping
    public VerificationEmailResponseDto verificationEmail(@RequestParam String email,
                                                          @RequestParam String code) {
        return mailService.verifiedCode(email, code);
    }
}
