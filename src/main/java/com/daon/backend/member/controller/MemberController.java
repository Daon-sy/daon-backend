package com.daon.backend.member.controller;

import com.daon.backend.member.dto.*;
import com.daon.backend.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Member", description = "Member domain API")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/members/username")
    public void checkUsername(@RequestBody @Valid CheckUsernameRequestDto requestDto) {
        memberService.checkUsername(requestDto.getUsername());
    }

    @Operation(summary = "회원가입", description = "회원가입 요청입니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/members/sign-up")
    public void signUp(@RequestBody @Valid SignUpRequestDto requestDto) {
        memberService.signUp(requestDto);
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정 요청입니다.")
    @PatchMapping("/api/members/me")
    public void modifyMember(@RequestBody @Valid ModifyMemberRequestDto requestDto){
        memberService.modifyMember(requestDto);
    }

    @Operation(summary = "나의 정보 조회", description = "나의 정보 조회 요청입니다.")
    @GetMapping("/api/members/me")
    public FindMemberResponseDto findMember() {

        return memberService.findMember();
    }

    @Operation(summary = "이메일 추가", description = "이메일 추가 요청입니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/members/me/emails")
    public void createEmail(@RequestBody @Valid AddEmailRequestDto requestDto) {
        memberService.createEmail(requestDto);
    }

    @Operation(summary = "이메일 목록 조회", description = "이메일 목록 조회 요청입니다.")
    @GetMapping("/api/members/me/emails")
    public FindEmailsResponseDto findEmails() {
        return memberService.findEmails();
    }

    @Operation(summary = "이메일 삭제", description = "이메일 삭제 요청입니다.")
    @DeleteMapping("/api/members/me/emails/{memberEmailId}")
    public void deleteEmail(@PathVariable Long memberEmailId) {
        memberService.deleteEmail(memberEmailId);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 요청입니다.")
    @DeleteMapping("/api/members/me")
    public ResponseEntity<Void> withdrawMember() {
        memberService.withdrawMember();
        ResponseCookie rtkCookie = ResponseCookie.from("rtk", "")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .headers(httpHeaders -> httpHeaders.add(HttpHeaders.SET_COOKIE, rtkCookie.toString()))
                .build();
    }
}

