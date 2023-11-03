package com.daon.backend.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Payload {

    private String id;
    private String role;
}
