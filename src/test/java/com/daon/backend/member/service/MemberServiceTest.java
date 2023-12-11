package com.daon.backend.member.service;

import com.daon.backend.config.MockConfig;
import com.daon.backend.member.domain.*;
import com.daon.backend.member.dto.AddEmailRequestDto;
import com.daon.backend.member.dto.FindEmailsResponseDto;
import com.daon.backend.member.dto.FindMemberResponseDto;
import com.daon.backend.member.dto.ModifyMemberRequestDto;
import com.daon.backend.security.MemberPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Transactional
@SpringBootTest
class MemberServiceTest extends MockConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    private Member member;

    private String savedMemberId;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username("user")
                .password("1234")
                .name("유저")
                .email("user@email.com")
                .passwordEncoder(passwordEncoder)
                .build();

        savedMemberId = memberRepository.save(member).getId();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        new MemberPrincipal(savedMemberId),
                        null
                )
        );
    }

    @DisplayName("회원 가입")
    @Test
    void signUp() {
        // then
        Member findMember = memberRepository.findById(savedMemberId).orElseThrow();

        assertEquals(member.getUsername(), findMember.getUsername());
        assertEquals(member.getPassword(), findMember.getPassword());
        assertEquals(member.getName(), findMember.getName());
    }

    @DisplayName("회원 정보 수정")
    @Test
    void modifyMember() {
        // given
        String name = "수정한 이름";
        ModifyMemberRequestDto requestDto = new ModifyMemberRequestDto(name, null, "1234");

        // when
        memberService.modifyMember(requestDto);

        // then
        Member findMember = memberRepository.findById(savedMemberId).orElseThrow();

        assertEquals(name, findMember.getName());
    }

    @DisplayName("회원(본인) 정보 조회")
    @Test
    void findMember() {
        // given
        String name = "유저";
        String username = "user";

        // when
        FindMemberResponseDto response = memberService.findMember();

        // then
        assertEquals(name, response.getName());
        assertEquals(username, response.getUsername());
    }

    @DisplayName("이메일 추가")
    @Test
    void createEmail() {
        // given
        String newEmail = "test@gmail.com";
        AddEmailRequestDto requestDto = new AddEmailRequestDto(newEmail);

        // when
        memberService.createEmail(requestDto);
        Member findMember = memberRepository.findById(savedMemberId).orElseThrow();

        // then
        assertEquals(2, findMember.getEmails().size());
    }

    @DisplayName("이메일 목록 조회")
    @Test
    void findEmails() {
        // given
        String newEmail = "test@gmail.com";
        AddEmailRequestDto requestDto = new AddEmailRequestDto(newEmail);

        // when
        memberService.createEmail(requestDto);
        FindEmailsResponseDto emails = memberService.findEmails();

        // then
        assertEquals(2, emails.getTotalCount());
        assertEquals(newEmail, emails.getMemberEmails().get(1).getEmail());
    }

    @DisplayName("이메일 삭제")
    @Test
    void deleteEmail() {
        // given
        String newEmail = "test@gmail.com";
        AddEmailRequestDto requestDto = new AddEmailRequestDto(newEmail);

        memberService.createEmail(requestDto);
        FindEmailsResponseDto emails = memberService.findEmails();
        Long emailId = emails.getMemberEmails().get(0).getMemberEmailId();

        // when
        memberService.deleteEmail(emailId);

        // then
        assertEquals(1, member.getEmails().size());
    }

    @DisplayName("회원 탈퇴")
    @Test
    void withdrawMember() {
        // when
        memberService.withdrawMember();

        // then
        assertThrows(MemberNotFoundException.class, () -> memberService.findMember());
    }
}