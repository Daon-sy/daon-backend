package com.daon.backend.mail.controller;

import com.daon.backend.mail.dto.SendVerificationEmailRequestDto;
import com.daon.backend.mail.dto.VerificationEmailCodeRequestDto;
import com.daon.backend.mail.dto.VerificationEmailResponseDto;
import com.daon.backend.mail.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/emails/verification")
@RestController
public class MailController {

    private final MailService mailService;

    @Operation(summary = "인증 메일 전송", description = "인증 메일 전송 요청입니다.")
    @PostMapping("/send")
    public void sendVerificationEmail(@RequestBody SendVerificationEmailRequestDto requestDto) {
        mailService.sendCodeToEmail(requestDto.getEmail());
    }
    @Operation(summary = "인증 번호 확인 조회", description = "인증 번호 확인 요청입니다.")
    @PostMapping("/check")
    public VerificationEmailResponseDto verificationEmail(@RequestBody VerificationEmailCodeRequestDto requestDto) {
        return mailService.verifiedCode(requestDto.getEmail(), requestDto.getCode());
    }
}
