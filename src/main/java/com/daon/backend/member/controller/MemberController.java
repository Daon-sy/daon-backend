package com.daon.backend.member.controller;

import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.member.dto.SignInRequestDto;
import com.daon.backend.member.dto.SignUpRequestDto;
import com.daon.backend.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Member", description = "Member domain API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberController {

    private final MemberService memberService;

    private static final String BEARER_TYPE = "Bearer ";

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

    @Operation(summary = "로그인", description = "로그인 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<CommonResponse<Void>> signIn(@RequestBody @Valid SignInRequestDto signInRequestDto) {
        String token = memberService.signIn(signInRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders -> httpHeaders.add(
                        HttpHeaders.AUTHORIZATION,
                        BEARER_TYPE + token
                ))
                .body(CommonResponse.createSuccess(null));
    }
}
