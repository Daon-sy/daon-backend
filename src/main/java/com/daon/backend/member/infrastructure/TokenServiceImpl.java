package com.daon.backend.member.infrastructure;

import com.daon.backend.jwt.domain.JwtService;
import com.daon.backend.member.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;

    @Override
    public String createMemberAccessToken(String memberId) {
        return jwtService.createMemberAccessToken(memberId);
    }
}
