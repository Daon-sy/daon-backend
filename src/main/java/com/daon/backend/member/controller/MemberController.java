package com.daon.backend.member.controller;

import com.daon.backend.common.response.ApiResponse;
import com.daon.backend.member.dto.SignInRequestDto;
import com.daon.backend.member.dto.SignUpRequestDto;
import com.daon.backend.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공")
    })
    @PostMapping("/sign-up")
    public ApiResponse<Void> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        memberService.signUp(signUpRequestDto);

        return ApiResponse.createSuccess(null);
    }

    @Operation(summary = "로그인", description = "로그인 요청입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<Void>> signIn(@RequestBody @Valid SignInRequestDto signInRequestDto) {
        String token = memberService.signIn(signInRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders -> httpHeaders.add(
                        HttpHeaders.AUTHORIZATION,
                        BEARER_TYPE + token
                ))
                .body(ApiResponse.createSuccess(null));
    }
}
