package com.daon.backend.member.service;

import com.daon.backend.member.domain.Member;
import com.daon.backend.member.infrastructure.MemberJpaRepository;
import com.daon.backend.member.dto.SignInRequestDto;
import com.daon.backend.member.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberJpaRepository memberRepository;

    /**
     * 회원가입
     */
    public UUID signUp(SignUpRequestDto signUpRequestDto) {
        return memberRepository.save(signUpRequestDto.toEntity()).getId();
    }

    /**
     * 로그인
     */
    public UUID signIn(SignInRequestDto signInRequestDto) {
        String requestEmail = signInRequestDto.getEmail();
        Member findMember = memberRepository.findByEmail(requestEmail)
                .orElseThrow(() -> new IllegalArgumentException("not found email: " + requestEmail));

        if (!findMember.getPassword().equals(signInRequestDto.getPassword())) {
            throw new IllegalArgumentException("password does not match");
        }

        return findMember.getId();
    }
}
