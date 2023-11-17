package com.daon.backend.member.controller;

import com.daon.backend.member.dto.FindMemberResponseDto;
import com.daon.backend.member.dto.ModifyMemberRequestDto;
import com.daon.backend.member.dto.SearchMemberResponseDto;
import com.daon.backend.member.dto.SignUpRequestDto;
import com.daon.backend.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Member", description = "Member domain API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원가입 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    public void signUp(@RequestBody @Valid SignUpRequestDto requestDto) {
        memberService.signUp(requestDto);
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공")
    })
    @PatchMapping("/me")
    public void modifyMember(@RequestBody ModifyMemberRequestDto requestDto){
        memberService.modifyMember(requestDto);
    }

    @Operation(summary = "나의 정보 조회", description = "나의 정보 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "나의 정보 조회 성공")
    })
    @GetMapping("/me")
    public FindMemberResponseDto findMember() {

        return memberService.findMember();
    }

    @Operation(summary = "회원 검색", description = "회원 검색 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 검색 성공")
    })
    @GetMapping
    public SearchMemberResponseDto searchMember(@RequestParam("username") String username) {

        return memberService.searchMember(username);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공")
    })
    @DeleteMapping("/me")
    public void withdrawMember() {
        memberService.withdrawMember();
    }
}
