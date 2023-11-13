package com.daon.backend.member.service;

import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberNotFoundException;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.domain.PasswordEncoder;
import com.daon.backend.member.dto.ModifyMemberDto;
import com.daon.backend.member.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final WorkspaceInitManager workspaceInitManager;

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (memberRepository.findByEmail(signUpRequestDto.getEmail()).isPresent()) {
            throw new AlreadyExistsMemberException(signUpRequestDto.getEmail());
        }

        Member member = signUpRequestDto.toEntity(passwordEncoder);
        memberRepository.save(member);
        workspaceInitManager.init(member.getId().toString(), member.getName());
    }

    @Transactional
    public void modifyMember(ModifyMemberDto dto, UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.byMemberId(memberId));
        member.modifyMember(dto.getEmail(), dto.getPassword(), dto.getName(), passwordEncoder);
    }
}
