package com.daon.backend.task.service;

import com.daon.backend.config.MockConfig;
import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.domain.PasswordEncoder;
import com.daon.backend.security.MemberPrincipal;
import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.domain.workspace.exception.WorkspaceNotFoundException;
import com.daon.backend.task.dto.workspace.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class WorkspaceServiceTest extends MockConfig {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    WorkspaceService workspaceService;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    EntityManager em;

    private Long workspaceId;

    private String savedMemberId;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
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

        CreateWorkspaceRequestDto requestDto = new CreateWorkspaceRequestDto(
                new CreateWorkspaceRequestDto.WorkspaceInfo(
                        "워크스페이스 제목",
                        null,
                        "워크스페이스 설명",
                        "워크스페이스 주제"
                ),
                new CreateWorkspaceRequestDto.WorkspaceProfileInfo(
                        "홍길동",
                        null,
                        "user@email.com"
                )
        );

        workspaceId = workspaceService.createWorkspace(requestDto).getWorkspaceId();
    }

    @DisplayName("워크스페이스 생성")
    @Test
    void createWorkspace() {
        // when
        FindWorkspaceResponseDto findWorkspace = workspaceService.findWorkspace(workspaceId);

        // then
        assertEquals("워크스페이스 제목", findWorkspace.getTitle());
        assertNull(findWorkspace.getImageUrl());
        assertEquals("워크스페이스 설명", findWorkspace.getDescription());
        assertEquals("워크스페이스 주제", findWorkspace.getSubject());
    }

    @DisplayName("워크스페이스 단건 조회")
    @Test
    void findWorkspace() {
        // when
        FindWorkspaceResponseDto responseDto = workspaceService.findWorkspace(workspaceId);

        // then
        assertEquals(workspaceId, responseDto.getWorkspaceId());
        assertEquals("워크스페이스 제목", responseDto.getTitle());
        assertNull(responseDto.getImageUrl());
        assertEquals("워크스페이스 설명", responseDto.getDescription());
        assertEquals("워크스페이스 주제", responseDto.getSubject());
        assertEquals("GROUP", responseDto.getDivision());
    }

    @DisplayName("워크스페이스 목록 조회")
    @Test
    void findWorkspaces() {
        // when
        FindWorkspacesResponseDto responseDto = workspaceService.findWorkspaces();

        System.out.println(responseDto.getWorkspaces().size());

        // then
        assertEquals(1, responseDto.getWorkspaces().size());
        assertEquals(workspaceId, responseDto.getWorkspaces().get(0).getWorkspaceId());
    }

    @DisplayName("워크스페이스 수정")
    @Test
    void modifyWorkspace() {
        // given
        String title = "수정된 제목";
        String description = "수정된 설명";
        ModifyWorkspaceRequestDto requestDto = new ModifyWorkspaceRequestDto(
                title,
                description,
                null,
                null
        );

        // when
        workspaceService.modifyWorkspace(requestDto, workspaceId);
        FindWorkspaceResponseDto responseDto = workspaceService.findWorkspace(workspaceId);

        // then
        assertEquals(workspaceId, responseDto.getWorkspaceId());
        assertEquals(title, responseDto.getTitle());
        assertEquals(description, responseDto.getDescription());
    }

    @DisplayName("워크스페이스 삭제")
    @Test
    void deleteWorkspace() {
        // when
        workspaceService.deleteWorkspace(workspaceId);

        // then
        assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.findWorkspace(workspaceId));
    }

    @DisplayName("프로필(본인) 조회")
    @Test
    void findProfile() {
        // when
        FindProfileResponseDto findProfile = workspaceService.findProfile(workspaceId);

        // then
        assertEquals("홍길동", findProfile.getName());
        assertNull(findProfile.getImageUrl());
        assertEquals("user@email.com", findProfile.getEmail());
    }

    @DisplayName("프로필(본인) 수정")
    @Test
    void modifyProfile() {
        // given
        String name = "둘리";
        String email = "ddochi@email.com";
        ModifyProfileRequestDto requestDto = new ModifyProfileRequestDto(name, null, email);

        // when
        workspaceService.modifyProfile(workspaceId, requestDto);
        FindProfileResponseDto findProfile = workspaceService.findProfile(workspaceId);

        // then
        assertEquals(name, findProfile.getName());
        assertEquals(email, findProfile.getEmail());
    }

    @DisplayName("워크스페이스 초대")
    @Test
    void inviteMember() {
        // given
        Member testMember = Member.builder()
                .username("test user")
                .password("1234")
                .name("테스트 이름")
                .email("testUser@email.com")
                .passwordEncoder(passwordEncoder)
                .build();
        memberRepository.save(testMember);

        InviteMemberRequestDto requestDto = new InviteMemberRequestDto("test user", Role.PROJECT_ADMIN);

        // when
        workspaceService.inviteMember(workspaceId, requestDto);
        Workspace findWorkspace = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow();

        // then
        assertEquals(1, findWorkspace.getInvitations().size());
    }

    @DisplayName("워크스페이스 참여")
    @Test
    void joinWorkspace() {
        // given
        Member testMember = Member.builder()
                .username("test user")
                .password("1234")
                .name("테스트 이름")
                .email("testUser@email.com")
                .passwordEncoder(passwordEncoder)
                .build();
        String testMemberId = memberRepository.save(testMember).getId();

        InviteMemberRequestDto request = new InviteMemberRequestDto("test user", Role.PROJECT_ADMIN);
        workspaceService.inviteMember(workspaceId, request);
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow();

        JoinWorkspaceRequestDto requestDto = new JoinWorkspaceRequestDto(
                "홍길동",
                null,
                "user2@email.com"
        );

        // when
        workspace.addParticipant(
                testMemberId,
                new Profile(
                        requestDto.getName(),
                        requestDto.getImageUrl(),
                        requestDto.getEmail()
                )
        );

        // then
        assertEquals(2, workspace.getWorkspaceParticipants().size());
    }

    @DisplayName("워크스페이스 탈퇴")
    @Test
    void withdrawWorkspace() {
        // given
        Member testMember = Member.builder()
                .username("test user")
                .password("1234")
                .name("테스트 이름")
                .email("testUser@email.com")
                .passwordEncoder(passwordEncoder)
                .build();
        String testMemberId = memberRepository.save(testMember).getId();

        InviteMemberRequestDto request = new InviteMemberRequestDto("test user", Role.WORKSPACE_ADMIN);
        workspaceService.inviteMember(workspaceId, request);
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow();

        JoinWorkspaceRequestDto requestDto = new JoinWorkspaceRequestDto(
                "홍길동",
                null,
                "user2@email.com"
        );

        workspace.addParticipant(
                testMemberId,
                new Profile(
                        requestDto.getName(),
                        requestDto.getImageUrl(),
                        requestDto.getEmail()
                )
        );

        // when
        workspaceService.withdrawWorkspace(workspaceId);
        FindWorkspaceParticipantsResponseDto response = workspaceService.findWorkspaceParticipants(workspaceId);

        // then
        assertEquals(1, response.getWorkspaceParticipants().size());
    }

    @DisplayName("워크스페이스 참여자 목록 조회")
    @Test
    void findWorkspaceParticipants() {
        // when
        FindWorkspaceParticipantsResponseDto responseDto = workspaceService.findWorkspaceParticipants(workspaceId);

        // then
        assertEquals(1, responseDto.getWorkspaceParticipants().size());
    }

    @DisplayName("워크스페이스 참여자 권한 변경")
    @Test
    void modifyParticipantRole() {
        // given
        Member testMember = Member.builder()
                .username("test user")
                .password("1234")
                .name("테스트 이름")
                .email("testUser@email.com")
                .passwordEncoder(passwordEncoder)
                .build();
        String testMemberId = memberRepository.save(testMember).getId();

        InviteMemberRequestDto request = new InviteMemberRequestDto("test user", Role.PROJECT_ADMIN);
        workspaceService.inviteMember(workspaceId, request);
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow();

        JoinWorkspaceRequestDto requestDto = new JoinWorkspaceRequestDto(
                "홍길동",
                null,
                "user2@email.com"
        );

        workspace.addParticipant(
                testMemberId,
                new Profile(
                        requestDto.getName(),
                        requestDto.getImageUrl(),
                        requestDto.getEmail()
                )
        );

        em.flush();

        WorkspaceParticipant workspaceParticipant = workspace.findWorkspaceParticipantByMemberId(testMemberId);

        ModifyRoleRequestDto modifyRoleRequestDto = new ModifyRoleRequestDto(
                workspaceParticipant.getId(),
                Role.BASIC_PARTICIPANT
        );

        // when
        workspaceService.modifyParticipantRole(modifyRoleRequestDto, workspaceId);

        // then
        assertEquals(Role.BASIC_PARTICIPANT, workspaceParticipant.getRole());
    }

    @DisplayName("워크스페이스 초기화")
    @Test
    void resetWorkspace() {
        // given
        Workspace workspace = Workspace.createOfPersonal(
                new WorkspaceCreator(savedMemberId, "홍길동", null, "user@email.com")
        );
        workspaceRepository.save(workspace);
        em.flush();

        Long newWorkspaceId = workspace.getId();
        String newTitle = "수정된 제목";
        ModifyWorkspaceRequestDto requestDto = new ModifyWorkspaceRequestDto(newTitle, null, null, null);
        workspaceService.modifyWorkspace(requestDto, newWorkspaceId);

        // when
        workspaceService.resetWorkspace(newWorkspaceId);

        // then
        assertNotEquals(newTitle, workspace.getTitle());
    }
}