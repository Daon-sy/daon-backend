package com.daon.backend.member.controller;

import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.member.dto.ModifyMemberDto;
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
import java.util.UUID;

@Tag(name = "Member", description = "Member domain API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원가입 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    public CommonResponse<Void> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        memberService.signUp(signUpRequestDto);

        return CommonResponse.createSuccess(null);
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공")
    })
    @PatchMapping("/members/{memberId}")
    public CommonResponse<Void> modifyMember(@RequestBody ModifyMemberDto requestDto ,
                                              @PathVariable UUID memberId){
        memberService.modifyMember(requestDto , memberId);
        return CommonResponse.createSuccess(null);
    }
}
