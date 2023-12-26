package com.daon.backend.mail.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendVerificationEmailRequestDto {

    private String email;
}
