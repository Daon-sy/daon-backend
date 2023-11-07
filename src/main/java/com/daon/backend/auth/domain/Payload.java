package com.daon.backend.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class Payload {

    private String id;
    private String role;
    private Instant issuedAt;
    private Instant expiredAt;
}
