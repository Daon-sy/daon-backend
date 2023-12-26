package com.daon.backend.auth.domain;

import com.daon.backend.auth.domain.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Tokens {

    private Token accessToken;
    private Token refreshToken;
}
