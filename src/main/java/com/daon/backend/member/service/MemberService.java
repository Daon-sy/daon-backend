package com.daon.backend.member.service;

import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.domain.PasswordEncoder;
import com.daon.backend.member.dto.SignInRequestDto;
import com.daon.backend.member.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (memberRepository.findByEmail(signUpRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("exists member: " + signUpRequestDto.getEmail());
        }

        Member member = signUpRequestDto.toEntity(passwordEncoder);
        memberRepository.save(member);
    }

    public String signIn(SignInRequestDto signInRequestDto) {
        String requestEmail = signInRequestDto.getEmail();
        Member findMember = memberRepository.findByEmail(requestEmail)
                .orElseThrow(() -> new BadCredentialsException("not found email: " + requestEmail));

        findMember.checkPassword(signInRequestDto.getPassword(), passwordEncoder);

        return tokenService.createMemberAccessToken(findMember.getId().toString());
    }
}
