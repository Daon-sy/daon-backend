package com.daon.backend.member.controller;

import com.daon.backend.member.dto.SignInRequestDto;
import com.daon.backend.member.dto.SignUpRequestDto;
import com.daon.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        memberService.signUp(signUpRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(@RequestBody @Valid SignInRequestDto signInRequestDto) {
        UUID result = memberService.signIn(signInRequestDto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Member-Id", result.toString());

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .build();
    }
}
