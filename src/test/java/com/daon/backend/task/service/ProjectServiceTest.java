package com.daon.backend.task.service;

import com.daon.backend.config.MockConfig;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import com.daon.backend.task.dto.project.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
@SpringBootTest
class ProjectServiceTest extends MockConfig {

    @MockBean
    SessionMemberProvider sessionMemberProvider;

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @BeforeEach
    void setUp() {
        BDDMockito.given(sessionMemberProvider.getMemberId()).willReturn("78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8");
    }

    @DisplayName("프로젝트 생성")
    @Test
    void createProject() {
        // given
        Long workspaceId = 3L;
        CreateProjectRequestDto requestDto =
                new CreateProjectRequestDto("프로젝트 제목", "프로젝트 설명");

        // when
        CreateProjectResponseDto responseDto = projectService.createProject(workspaceId, requestDto);

        // then
        Project findProject = projectRepository.findProjectById(responseDto.getProjectId()).orElseThrow();
        assertEquals("프로젝트 제목", findProject.getTitle());
        assertEquals("프로젝트 설명", findProject.getDescription());
    }

    @DisplayName("프로젝트 단건 조회")
    @Test
    void findProject() {
        // given
        Long projectId = 1L;

        // when
        FindProjectResponseDto responseDto = projectService.findProject(projectId);

        // then
        assertEquals(projectId, responseDto.getProjectId());
        assertEquals("project1", responseDto.getTitle());
        assertNull(responseDto.getDescription());
    }

    @DisplayName("프로젝트 목록 조회")
    @Test
    void findProjects() {
        // given
        Long workspaceId = 3L;

        // when
        FindProjectsResponseDto responseDto = projectService.findProjects(workspaceId);

        // then
        assertEquals(1, responseDto.getProjects().size());
        assertEquals(workspaceId, responseDto.getWorkspaceId());
    }

    @DisplayName("프로젝트 수정")
    @Test
    void modifyProject() {
        // given
        Long projectId = 1L;
        String title = "수정된 제목";
        String description = "수정된 설명";
        ModifyProjectRequestDto requestDto = new ModifyProjectRequestDto(title, description);

        // when
        projectService.modifyProject(projectId, requestDto);
        Project project = projectRepository.findProjectById(projectId).orElseThrow();

        // then
        assertEquals(title, project.getTitle());
        assertEquals(description, project.getDescription());
    }

    @DisplayName("프로젝트 초대")
    @Test
    void inviteWorkspaceParticipant() {
        // given
        Long workspaceId = 3L;
        Long projectId = 1L;
        Long workspaceParticipantId = 4L;

        InviteWorkspaceParticipantRequestDto requestDtoForInvite =
                new InviteWorkspaceParticipantRequestDto(workspaceParticipantId);

        // when
        projectService.inviteWorkspaceParticipant(workspaceId, projectId, requestDtoForInvite);
        List<ProjectParticipant> projectParticipants = projectRepository.findProjectParticipantsByProjectId(projectId);

        // then
        assertEquals(3, projectParticipants.size());
    }

    @DisplayName("프로젝트 나의 정보 조회")
    @Test
    void findMyProfile() {
        // given
        Long projectId = 1L;

        // when
        FindMyProfileResponseDto responseDto = projectService.findMyProfile(projectId);

        // then
        assertEquals("WS_USER1", responseDto.getName());
        assertEquals("user1@email.com", responseDto.getEmail());
    }

    @DisplayName("프로젝트 참여자 목록 조회")
    @Test
    void findProjectParticipants() {
        // given
        Long projectId = 1L;

        // when
        FindProjectParticipantsResponseDto projectParticipants = projectService.findProjectParticipants(projectId);

        // then
        assertEquals(2, projectParticipants.getProjectParticipants().size());
    }

    @DisplayName("프로젝트 참여자 강퇴")
    @Test
    void deportProjectParticipant() {
        // given
        Long projectId = 1L;
        Long projectParticipantId = 2L;
        DeportProjectParticipantRequestDto requestDto = new DeportProjectParticipantRequestDto(projectParticipantId);

        // when
        projectService.deportProjectParticipant(projectId, requestDto);

        // then
        List<ProjectParticipant> projectParticipants = projectRepository.findProjectParticipantsByProjectId(projectId);
        assertEquals(1, projectParticipants.size());
    }

    @DisplayName("프로젝트 탈퇴")
    @Test
    void withdrawProject() {
        // given
        Long projectId = 1L;

        // when
        projectService.withdrawProject(projectId);

        // then
        List<ProjectParticipant> projectParticipants = projectRepository.findProjectParticipantsByProjectId(projectId);
        assertEquals(1, projectParticipants.size());
    }

    @DisplayName("프로젝트 삭제")
    @Test
    void deleteProject() {
        // given
        Long workspaceId = 3L;
        Long projectId = 1L;

        // when
        projectService.deleteProject(projectId);

        // then
        Workspace findWorkspace = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow();
        assertEquals(1, findWorkspace.getProjects().stream().filter(Project::isRemoved).count());
    }
}