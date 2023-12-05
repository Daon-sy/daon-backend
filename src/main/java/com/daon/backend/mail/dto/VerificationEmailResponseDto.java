package com.daon.backend.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerificationEmailResponseDto {

    private boolean isVerified;
}
