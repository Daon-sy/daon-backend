package com.daon.backend.jwt.domain;

public interface JwtService {

    boolean validate(String token);

    String createMemberAccessToken(String memberId);

    // Todo 서비스 관리자 도입시 적용
    String createAdminAccessToken(String adminId);

    Payload parse(String token);
}
