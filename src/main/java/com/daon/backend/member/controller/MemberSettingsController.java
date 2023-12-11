package com.daon.backend.member.controller;

import com.daon.backend.member.dto.MemberSettingsResponseDto;
import com.daon.backend.member.dto.ModifyMemberSettingsRequestDto;
import com.daon.backend.member.service.MemberSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/settings")
public class MemberSettingsController {

    private final MemberSettingsService memberSettingsService;

    @Operation(summary = "회원 설정 정보 조회", description = "회원 설정 정보 조회 요청입니다.")
    @GetMapping
    public MemberSettingsResponseDto findMemberSettings() {
        return memberSettingsService.findMemberSettings();
    }

    @Operation(summary = "회원 설정 정보 수정", description = "회원 설정 정보 수정 요청입니다.")
    @PostMapping
    public void modifyMemberSettings(@RequestBody ModifyMemberSettingsRequestDto requestDto) {
        memberSettingsService.modifyMemberSettings(requestDto);
    }
}
