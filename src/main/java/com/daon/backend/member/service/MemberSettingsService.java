package com.daon.backend.member.service;

import com.daon.backend.member.domain.MemberNotFoundException;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.domain.Settings;
import com.daon.backend.member.dto.MemberSettingsResponseDto;
import com.daon.backend.member.dto.ModifyMemberSettingsRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberSettingsService {

    private final MemberRepository memberRepository;
    private final SessionMemberProvider sessionMemberProvider;

    public MemberSettingsResponseDto findMemberSettings() {
        String memberId = sessionMemberProvider.getMemberId();
        return memberRepository.findSettingsByMemberId(memberId)
                .orElseThrow(() -> MemberNotFoundException.byMemberId(memberId));
    }

    @Transactional
    public void modifyMemberSettings(ModifyMemberSettingsRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.byMemberId(memberId))
                .changeSettings(new Settings(requestDto.getNotified()));
    }
}
