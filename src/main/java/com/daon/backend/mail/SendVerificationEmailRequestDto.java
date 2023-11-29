package com.daon.backend.mail;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendVerificationEmailRequestDto {

    private String email;
}
