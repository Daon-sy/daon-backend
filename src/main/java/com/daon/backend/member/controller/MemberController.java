package com.daon.backend.member.controller;

import com.daon.backend.common.response.ApiResponse;
import com.daon.backend.member.dto.SignInRequestDto;
import com.daon.backend.member.dto.SignUpRequestDto;
import com.daon.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberController {

    private static final String BEARER_TYPE = "Bearer ";

    private final MemberService memberService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    public ApiResponse<Void> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        memberService.signUp(signUpRequestDto);

        return ApiResponse.createSuccess(null);
    }

    // 응답 헤더에 접근해야 해서 예외적으로 ResponseEntity 사용
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
