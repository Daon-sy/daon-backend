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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionMemberProvider sessionMemberProvider;
    private final MemberServiceThroughTask memberServiceThroughTask;

    /**
     * 회원 가입
     */
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

    /**
     * 회원 정보 수정
     */
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

    /**
     * 회원(본인) 정보 조회
     */
    public FindMemberResponseDto findMember() {
        String memberId = sessionMemberProvider.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.byMemberId(memberId));

        return new FindMemberResponseDto(member);
    }

    /**
     * 회원 검색
     */
    public SearchMemberResponseDto searchMember(String username) {
        List<MemberSummary> memberSummaries = memberRepository.searchMembersByUsername(username);

        return new SearchMemberResponseDto(memberSummaries);
    }

    /**
     * 이메일 추가
     */
    @Transactional
    public void createEmail(AddEmailRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.byMemberId(memberId));

        String email = requestDto.getEmail();
        member.createEmail(email);
    }

    /**
     * 이메일 목록 조회
     */
    public FindEmailsResponseDto findEmails() {
        String memberId = sessionMemberProvider.getMemberId();
        Member findmember = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.byMemberId(memberId));

        return new FindEmailsResponseDto(
                findmember.getEmails().stream()
                        .map(FindEmailsResponseDto.EmailInfo::new)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 이메일 삭제
     */
    @Transactional
    public void deleteEmail(Long memberEmailId) {
        String memberId = sessionMemberProvider.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.byMemberId(memberId));
        member.removeEmail(memberEmailId);
    }

    /**
     * 회원 탈퇴
     */
    public void withdrawMember() {
        String memberId = sessionMemberProvider.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.byMemberId(memberId));

        memberServiceThroughTask.deleteRelatedTaskDomains(memberId);
        member.withdrawMember();
    }
}
