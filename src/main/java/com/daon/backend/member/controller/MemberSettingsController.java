package com.daon.backend.member.controller;

import com.daon.backend.member.dto.MemberSettingsResponseDto;
import com.daon.backend.member.dto.ModifyMemberSettingsRequestDto;
import com.daon.backend.member.service.MemberSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/settings")
public class MemberSettingsController {

    private final MemberSettingsService memberSettingsService;

    @GetMapping
    public MemberSettingsResponseDto findMemberSettings() {
        return memberSettingsService.findMemberSettings();
    }

    @PostMapping
    public void modifyMemberSettings(@RequestBody ModifyMemberSettingsRequestDto requestDto) {
        memberSettingsService.modifyMemberSettings(requestDto);
    }
}
