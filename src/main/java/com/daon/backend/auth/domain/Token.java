package com.daon.backend.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class Token {

    private String value;
    private Instant issuedAt;
    private Instant expiredAt;
}
