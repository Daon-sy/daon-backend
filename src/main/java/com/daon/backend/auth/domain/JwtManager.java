package com.daon.backend.auth.domain;

public interface JwtManager {

    boolean validate(String token);

    Token createMemberAccessToken(String memberId);
    Token createMemberRefreshToken(String memberId);

    Payload parse(String token);

    Tokens issueTokens(String memberId);
    Tokens reissueTokens(String refreshToken);

    void prohibitTokens(String accessToken, String refreshToken);
}
