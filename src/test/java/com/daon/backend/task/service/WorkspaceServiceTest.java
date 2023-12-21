package com.daon.backend.task.service;

import com.daon.backend.config.MockConfig;
import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.domain.workspace.exception.WorkspaceNotFoundException;
import com.daon.backend.task.dto.workspace.*;
import com.daon.backend.task.infrastructure.workspace.WorkspaceJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Slf4j
@Transactional
@SpringBootTest
class WorkspaceServiceTest extends MockConfig {

    @MockBean
    SessionMemberProvider sessionMemberProvider;

    @Autowired
    WorkspaceService workspaceService;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    WorkspaceJpaRepository workspaceJpaRepository;

    final String wsAdminMemberId = "78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8";
    final String wsBasicParticipantMemberId = "4c624615-7123-4a63-9ade-0fd5889452cd";

    @BeforeEach
    void setUp() {
        BDDMockito.given(sessionMemberProvider.getMemberId()).willReturn(wsAdminMemberId);
    }

    @DisplayName("워크스페이스 생성")
    @Test
    void createWorkspace() {
        // given
        String title = "워크스페이스 제목";
        String description = "워크스페이스 설명";
        String subject = "워크스페이스 주제";
        String name = "홍길동";
        String email = "test1@email.com";
        CreateWorkspaceRequestDto requestDto = new CreateWorkspaceRequestDto(
                new CreateWorkspaceRequestDto.WorkspaceInfo(
                        title,
                        null,
                        description,
                        subject
                ),
                new CreateWorkspaceRequestDto.WorkspaceProfileInfo(
                        name,
                        null,
                        email
                )
        );

        // when
        CreateWorkspaceResponseDto responseDto = workspaceService.createWorkspace(requestDto);
        Workspace findWorkspace = workspaceRepository.findById(responseDto.getWorkspaceId()).orElseThrow();

        // then
        assertThat(findWorkspace.getTitle()).isEqualTo(title);
        assertThat(findWorkspace.getDescription()).isEqualTo(description);
        assertThat(findWorkspace.getImageUrl()).isNull();
        assertThat(findWorkspace.getSubject()).isEqualTo(subject);
        assertThat(findWorkspace.getWorkspaceParticipants().get(0).getProfile().getName()).isEqualTo(name);
        assertThat(findWorkspace.getWorkspaceParticipants().get(0).getProfile().getEmail()).isEqualTo(email);
    }

    @DisplayName("워크스페이스 단건 조회")
    @Test
    void findWorkspace() {
        // given
        Long workspaceId = 1L;

        // when
        FindWorkspaceResponseDto responseDto = workspaceService.findWorkspace(workspaceId);

        // then
        assertThat(responseDto.getWorkspaceId()).isEqualTo(workspaceId);
        assertThat(responseDto.getDivision()).isEqualTo("PERSONAL");
    }

    @DisplayName("워크스페이스 목록 조회")
    @Test
    void findWorkspaces() {
        // when
        FindWorkspacesResponseDto responseDto = workspaceService.findWorkspaces();

        // then
        assertThat(responseDto.getWorkspaces().size()).isEqualTo(2);
    }

    @DisplayName("워크스페이스 수정")
    @Test
    void modifyWorkspace() {
        // given
        Long workspaceId = 1L;
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
        assertThat(responseDto.getWorkspaceId()).isEqualTo(workspaceId);
        assertThat(responseDto.getTitle()).isEqualTo(title);
        assertThat(responseDto.getDescription()).isEqualTo(description);
    }

    @DisplayName("워크스페이스 삭제")
    @Test
    void deleteWorkspace() {
        System.out.println(
                workspaceJpaRepository.findAll().stream().map(Workspace::getId).collect(Collectors.toList())
        );

        // given
        Long workspaceId = 3L;

        // when
        workspaceService.deleteWorkspace(workspaceId);

        // then
        assertThatExceptionOfType(WorkspaceNotFoundException.class)
                .isThrownBy(() -> workspaceService.findWorkspace(workspaceId));
    }

    @DisplayName("프로필(본인) 조회")
    @Test
    void findProfile() {
        // given
        Long workspaceId = 1L;

        // when
        FindProfileResponseDto findProfile = workspaceService.findProfile(workspaceId);

        // then
        assertThat(findProfile.getName()).isEqualTo("USER1");
        assertThat(findProfile.getImageUrl()).isNull();
        assertThat(findProfile.getEmail()).isEqualTo("user1@email.com");
    }

    @DisplayName("프로필(본인) 수정")
    @Test
    void modifyProfile() {
        // given
        Long workspaceId = 1L;
        String name = "둘리";
        String email = "ddochi@email.com";
        ModifyProfileRequestDto requestDto = new ModifyProfileRequestDto(name, null, email);

        // when
        workspaceService.modifyProfile(workspaceId, requestDto);
        FindProfileResponseDto findProfile = workspaceService.findProfile(workspaceId);

        // then
        assertThat(findProfile.getName()).isEqualTo(name);
        assertThat(findProfile.getEmail()).isEqualTo(email);
    }

    @DisplayName("워크스페이스 초대")
    @Test
    void inviteMember() {
        // given
        Long workspaceId = 3L;

        InviteMemberRequestDto requestDto = new InviteMemberRequestDto("user4", Role.PROJECT_ADMIN);

        // when
        workspaceService.inviteMember(workspaceId, requestDto);
        Workspace findWorkspace = workspaceRepository.findById(workspaceId).orElseThrow();

        // then
        assertThat(findWorkspace.getInvitations().size()).isEqualTo(2);
    }

    @DisplayName("워크스페이스 참여")
    @Test
    void joinWorkspace() {
        // given
        Long workspaceId = 3L;
        String testMemberId = "2831ccac-aef9-4359-abbb-1d432b1b8078";
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow();

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
        assertThat(workspace.getWorkspaceParticipants().size()).isEqualTo(4);
    }

    @DisplayName("워크스페이스 탈퇴 - 탈퇴할 사용자 본인만 WORKSPACE_ADMIN이 이면, 워크스페이스가 삭제된다.")
    @Test
    void withdrawWorkspace_WORKSPACE_ADMIN() {
        // given
        Long workspaceId = 3L;

        // when
        workspaceService.withdrawWorkspace(workspaceId);

        // then
        assertThatExceptionOfType(WorkspaceNotFoundException.class)
                .isThrownBy(() -> workspaceService.findWorkspace(workspaceId));
    }

    @Disabled
    @DisplayName("워크스페이스 탈퇴 - BASIC_PARTICIPANT가 탈퇴하면 해당 사용자만 삭제되고 워크스페이스는 삭제되지 않는다.")
    @Test
    void withdrawWorkspace_BASIC_PARTICIPANT() {
        // given
        BDDMockito.given(sessionMemberProvider.getMemberId())
                .willReturn(wsBasicParticipantMemberId);
        Long workspaceId = 3L;
        Workspace workspaceBeforeTest = workspaceJpaRepository.findById(workspaceId).orElseThrow();
        int participantSizeBeforeTest = workspaceBeforeTest.getParticipants().size();

        // when
        workspaceService.withdrawWorkspace(workspaceId);

        // then
        Workspace resultWorkspace = workspaceJpaRepository.findById(workspaceId).orElseThrow();
        assertThat(resultWorkspace.getWorkspaceParticipants().size()).isEqualTo(participantSizeBeforeTest - 1);
        assertThat(resultWorkspace.getWorkspaceParticipants().stream()
                .anyMatch(wsp -> wsp.memberIdEquals(wsBasicParticipantMemberId))).isFalse();
    }

    @DisplayName("워크스페이스 참여자 목록 조회")
    @Test
    void findWorkspaceParticipants() {
        // given
        Long workspaceId = 3L;

        // when
        FindWorkspaceParticipantsResponseDto responseDto = workspaceService.findWorkspaceParticipants(workspaceId);

        // then
        assertThat(responseDto.getWorkspaceParticipants().size()).isEqualTo(3);
    }

    @DisplayName("워크스페이스 참여자 권한 변경")
    @Test
    void modifyParticipantRole() {
        // given
        Long workspaceId = 3L;
        Long workspaceParticipantId = 4L;

        ModifyRoleRequestDto modifyRoleRequestDto = new ModifyRoleRequestDto(
                workspaceParticipantId,
                Role.BASIC_PARTICIPANT
        );
        WorkspaceParticipant workspaceParticipant =
                workspaceJpaRepository.findById(workspaceId).orElseThrow().getWorkspaceParticipants().stream()
                        .filter(w -> w.getId().equals(workspaceParticipantId))
                        .findFirst()
                        .orElseThrow();

        // when
        workspaceService.modifyParticipantRole(modifyRoleRequestDto, workspaceId);

        // then
        assertThat(workspaceParticipant.getRole()).isEqualTo(Role.BASIC_PARTICIPANT);
    }

    @DisplayName("워크스페이스 초기화")
    @Test
    void resetWorkspace() {
        // given
        Long workspaceId = 1L;
        String title = "USER1님의 개인 워크스페이스 공간";
        Workspace findWorkspace = workspaceRepository.findById(workspaceId).orElseThrow();

        // when
        workspaceService.resetWorkspace(workspaceId);

        // then
        assertThat(findWorkspace.getTitle()).isEqualTo(title);
    }
}
