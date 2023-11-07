package com.daon.backend.auth.infrastructure;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtProperties {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpSec;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpSec;

    @Value("${jwt.refresh-token.reissue-condition}")
    private Long refreshTokenReissueCondSec;
}
