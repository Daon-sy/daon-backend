package com.daon.backend.member.service;

public interface TokenService {

    String createMemberAccessToken(String memberId);
}
