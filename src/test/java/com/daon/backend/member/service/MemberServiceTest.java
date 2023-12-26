package com.daon.backend.member.service;

import com.daon.backend.config.MockConfig;
import com.daon.backend.member.domain.Member;
import com.daon.backend.member.dto.*;
import com.daon.backend.member.infrastructure.MemberJpaRepository;
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

import static org.assertj.core.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class MemberServiceTest extends MockConfig {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        new MemberPrincipal("78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8"),
                        null,
                        null
                )
        );

        member = memberJpaRepository.findAll().stream().
                filter(m -> m.getUsername().equals("user1")).findFirst().orElseThrow();
    }

    @DisplayName("회원 가입")
    @Test
    void signUp() {
        // given
        String username = "testUser";
        String name = "유저";
        String email = "test@email.com";
        SignUpRequestDto requestDto = new SignUpRequestDto(
                username,
                "1234",
                name,
                email
        );

        // when
        memberService.signUp(requestDto);

        Member findMember = memberJpaRepository.findAll().stream().
                filter(m -> m.getUsername().equals("testUser")).findFirst().orElseThrow();

        // then
        assertThat(findMember.getUsername()).isEqualTo(username);
        assertThat(findMember.getName()).isEqualTo(name);
        assertThat(findMember.getEmails().size()).isEqualTo(1);
    }

    @DisplayName("회원 아이디 중복 확인")
    @Test
    void checkUsername() {
        // given
        String testUsername = "user1";

        // when then
        assertThatExceptionOfType(AlreadyExistsMemberException.class)
                .isThrownBy(() -> memberService.checkUsername(testUsername));
    }

    @DisplayName("회원 정보 수정")
    @Test
    void modifyMember() {
        // given
        String editName = "수정한 이름";
        ModifyMemberRequestDto requestDto = new ModifyMemberRequestDto(editName, null, "123123");

        // when
        memberService.modifyMember(requestDto);

        // then
        assertThat(member.getName()).isEqualTo(editName);
    }

    @DisplayName("회원(본인) 정보 조회")
    @Test
    void findMember() {
        // given
        String name = "USER1";
        String username = "user1";

        // when
        FindMemberResponseDto responseDto = memberService.findMember();

        // then
        assertThat(responseDto.getName()).isEqualTo(name);
        assertThat(responseDto.getUsername()).isEqualTo(username);
    }

    @DisplayName("이메일 추가")
    @Test
    void createEmail() {
        // given
        String newEmail = "test@gmail.com";
        AddEmailRequestDto requestDto = new AddEmailRequestDto(newEmail);

        // when
        memberService.createEmail(requestDto);

        // then
        assertThat(member.getEmails().size()).isEqualTo(2);
        assertThat(member.getEmails().get(1).getEmail()).isEqualTo(newEmail);
    }

    @DisplayName("이메일 목록 조회")
    @Test
    void findEmails() {
        // given
        String newEmail = "test@gmail.com";
        AddEmailRequestDto requestDto = new AddEmailRequestDto(newEmail);

        // when
        memberService.createEmail(requestDto);
        FindEmailsResponseDto responseDto = memberService.findEmails();

        // then
        assertThat(responseDto.getTotalCount()).isEqualTo(2);
        assertThat(responseDto.getMemberEmails().get(1).getEmail()).isEqualTo(newEmail);
    }

    @DisplayName("이메일 삭제")
    @Test
    void deleteEmail() {
        // given
        String newEmail = "test@gmail.com";
        AddEmailRequestDto requestDto = new AddEmailRequestDto(newEmail);

        memberService.createEmail(requestDto);
        FindEmailsResponseDto responseDto = memberService.findEmails();
        Long emailId = responseDto.getMemberEmails().get(0).getMemberEmailId();

        // when
        memberService.deleteEmail(emailId);

        // then
        assertThat(member.getEmails().size()).isEqualTo(1);
    }

    @DisplayName("회원 탈퇴")
    @Test
    void withdrawMember() {
        // given when
        memberService.withdrawMember();

        // then
        int countMembers = memberJpaRepository.findAll().size();
        assertThat(countMembers).isEqualTo(3);
    }
}
