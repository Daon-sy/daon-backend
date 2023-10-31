package com.daon.backend.member.controller;

import com.daon.backend.member.dto.SignInRequestDto;
import com.daon.backend.member.dto.SignUpRequestDto;
import com.daon.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<UUID> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        UUID result = memberService.signUp(signUpRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UUID> signIn(@RequestBody SignInRequestDto signInRequestDto) {
        UUID result = memberService.signIn(signInRequestDto);

        return ResponseEntity.ok().body(result);
    }
}
