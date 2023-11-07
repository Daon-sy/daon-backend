package com.daon.backend.member.controller;

import com.daon.backend.auth.dto.SignInRequestDto;
import com.daon.backend.member.dto.SignUpRequestDto;
import com.daon.backend.member.infrastructure.MemberJpaRepository;
import com.daon.backend.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    MemberJpaRepository memberRepository;

    @Autowired
    MemberService memberService;

    @BeforeEach
    void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        memberRepository.deleteAll();
    }

    @DisplayName("회원가입")
    @Test
    void signUp() throws Exception {
        // given
        final String url = "/api/sign-up";
        final SignUpRequestDto requestDto = getSignUpRequestDto();
        final String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());
    }

    @DisplayName("로그인")
    @Test
    void signIn() throws Exception {
        // given
        final String url = "/api/sign-in";
        final SignUpRequestDto signUpRequestDto = getSignUpRequestDto();
        memberService.signUp(signUpRequestDto);

        final String loginId = "test@email.com";
        final String loginPassword = "1234";
        final SignInRequestDto requestDto = new SignInRequestDto(loginId, loginPassword);
        final String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", notNullValue()));
    }

    private static SignUpRequestDto getSignUpRequestDto() {
        final String email = "test@email.com";
        final String password = "1234";
        final String name = "홍길동";

        return SignUpRequestDto.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
}
