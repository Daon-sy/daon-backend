package com.daon.backend.auth.service;

import com.daon.backend.auth.domain.JwtManager;
import com.daon.backend.auth.domain.Tokens;
import com.daon.backend.auth.dto.SignInRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final JwtManager jwtManager;
    private final MemberDetailsService memberDetailsService;

    public Tokens signIn(SignInRequestDto requestDto) {
        MemberDetails memberDetails = memberDetailsService.signIn(requestDto.getUsername(), requestDto.getPassword());
        return jwtManager.issueTokens(memberDetails.getMemberId());
    }

    public Tokens reissue(String refreshToken) {
        return jwtManager.reissueTokens(refreshToken);
    }
}
