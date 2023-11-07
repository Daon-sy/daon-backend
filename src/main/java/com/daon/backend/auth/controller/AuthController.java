package com.daon.backend.auth.controller;

import com.daon.backend.auth.domain.UnauthorizedException;
import com.daon.backend.auth.service.AuthService;
import com.daon.backend.auth.domain.Tokens;
import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.auth.dto.SignInRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private static final String BEARER_TYPE = "Bearer ";

    private final AuthService authService;

    @Operation(summary = "로그인", description = "로그인 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<CommonResponse<Void>> signIn(@RequestBody @Valid SignInRequestDto signInRequestDto) {
        Tokens tokens = authService.signIn(signInRequestDto);
        ResponseCookie rtkCookie = ResponseCookie.from("rtk", tokens.getRefreshToken().getValue())
                .path("/")
                .httpOnly(true)
                .maxAge(Duration.between(Instant.now(), tokens.getRefreshToken().getExpiredAt()))
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders -> {
                    httpHeaders.add(
                            HttpHeaders.AUTHORIZATION,
                            BEARER_TYPE + tokens.getAccessToken().getValue()
                    );
                    httpHeaders.add(
                            HttpHeaders.SET_COOKIE,
                            rtkCookie.toString()
                    );
                })
                .body(CommonResponse.createSuccess(null));
    }

    @Operation(summary = "엑세스 토큰 재발급", description = "엑세스 토큰을 재발급하여 인증 헤더에 담아줍니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재발급 성공")
    })
    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse<Void>> reissueToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) throw new UnauthorizedException();
        String refreshTokenValue = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("rtk"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(UnauthorizedException::new);

        Tokens tokens = authService.reissue(refreshTokenValue);
        String rtkCookie = tokens.getRefreshToken() != null ? ResponseCookie.from("rtk", tokens.getRefreshToken().getValue())
                .path("/")
                .httpOnly(true)
                .maxAge(Duration.between(Instant.now(), tokens.getRefreshToken().getExpiredAt()).getSeconds())
                .build()
                .toString()
                : null;

        log.info("토큰 재발급 완료");

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders -> {
                    httpHeaders.add(
                            HttpHeaders.AUTHORIZATION,
                            BEARER_TYPE + tokens.getAccessToken().getValue()
                    );
                    if (rtkCookie != null) {
                        httpHeaders.add(
                                HttpHeaders.SET_COOKIE,
                                rtkCookie
                        );
                    }
                })
                .body(CommonResponse.createSuccess(null));
    }
}