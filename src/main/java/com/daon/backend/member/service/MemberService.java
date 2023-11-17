package com.daon.backend.member.service;

import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberNotFoundException;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.domain.PasswordEncoder;
import com.daon.backend.member.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionMemberProvider sessionMemberProvider;
    private final MemberServiceThroughTask memberServiceThroughTask;

    @Transactional
    public void signUp(SignUpRequestDto requestDto) {
        memberRepository.findByUsername(requestDto.getUsername())
                .ifPresent(member -> {
                    throw new AlreadyExistsMemberException(requestDto.getUsername());
                });

        memberRepository.save(
                Member.builder()
                        .username(requestDto.getUsername())
                        .password(requestDto.getPassword())
                        .name(requestDto.getName())
                        .email(requestDto.getEmail())
                        .passwordEncoder(passwordEncoder)
                        .build()
        );
    }

    @Transactional
    public void modifyMember(ModifyMemberRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.byMemberId(memberId))
                .modifyMember(
                        requestDto.getPrevPassword(),
                        requestDto.getNewPassword(),
                        requestDto.getName(),
                        passwordEncoder
                );
    }

    public FindMemberResponseDto findMember() {
        String memberId = sessionMemberProvider.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.byMemberId(memberId));

        return new FindMemberResponseDto(member);
    }

    public SearchMemberResponseDto searchMember(String username) {
        List<MemberSummary> memberSummaries = memberRepository.searchMembersByUsername(username);

        return new SearchMemberResponseDto(memberSummaries);
    }

    @Transactional
    public void withdrawMember() {
        String memberId = sessionMemberProvider.getMemberId();
        memberServiceThroughTask.deleteRelatedTaskDomains(memberId);
    }
}
