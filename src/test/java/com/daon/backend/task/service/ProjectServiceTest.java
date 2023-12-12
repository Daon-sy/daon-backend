package com.daon.backend.task.service;

import com.daon.backend.config.MockConfig;
import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.domain.PasswordEncoder;
import com.daon.backend.security.MemberPrincipal;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectNotFoundException;
import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.dto.project.*;
import com.daon.backend.task.dto.workspace.CreateWorkspaceRequestDto;
import com.daon.backend.task.dto.workspace.InviteMemberRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class ProjectServiceTest extends MockConfig {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    WorkspaceService workspaceService;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    EntityManager em;

    private Long workspaceId;

    private Long projectId;

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

        CreateProjectRequestDto createProjectRequestDto =
                new CreateProjectRequestDto("프로젝트 제목", "프로젝트 설명");
        projectId = projectService.createProject(workspaceId, createProjectRequestDto).getProjectId();

        em.clear();
    }

    @DisplayName("프로젝트 생성")
    @Test
    void createProject() {
        // when
        Project project = projectRepository.findProjectById(projectId).orElseThrow();

        // then
        assertEquals("프로젝트 제목", project.getTitle());
        assertEquals("프로젝트 설명", project.getDescription());
    }

    @DisplayName("프로젝트 단건 조회")
    @Test
    void findProject() {
        // when
        FindProjectResponseDto responseDto = projectService.findProject(projectId);

        // then
        assertEquals(projectId, responseDto.getProjectId());
        assertEquals("프로젝트 제목", responseDto.getTitle());
        assertEquals("프로젝트 설명", responseDto.getDescription());
    }

    @DisplayName("프로젝트 목록 조회")
    @Test
    void findProjects() {
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

        workspace.addParticipant(
                testMemberId,
                new Profile(
                        "홍길동",
                        null,
                        "user1@email.com"
                )
        );
        em.flush();

        Long workspaceParticipantId = workspace.findWorkspaceParticipantByMemberId(testMemberId).getId();
        InviteWorkspaceParticipantRequestDto requestDtoForInvite =
                new InviteWorkspaceParticipantRequestDto(workspaceParticipantId);

        // when
        projectService.inviteWorkspaceParticipant(workspaceId, projectId, requestDtoForInvite);
        List<ProjectParticipant> projectParticipants = projectRepository.findProjectParticipantsByProjectId(projectId);

        // then
        assertEquals(2, projectParticipants.size());
    }

    @DisplayName("프로젝트 나의 정보 조회")
    @Test
    void findMyProfile() {
        // when
        FindMyProfileResponseDto responseDto = projectService.findMyProfile(projectId);

        // then
        assertEquals("홍길동", responseDto.getName());
        assertEquals("user@email.com", responseDto.getEmail());
    }

    @DisplayName("프로젝트 참여자 목록 조회")
    @Test
    void findProjectParticipants() {
        // when
        FindProjectParticipantsResponseDto projectParticipants = projectService.findProjectParticipants(projectId);

        // then
        assertEquals(1, projectParticipants.getProjectParticipants().size());
    }

    @DisplayName("프로젝트 참여자 강퇴")
    @Test
    void deportProjectParticipant() {
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

        workspace.addParticipant(
                testMemberId,
                new Profile(
                        "홍길동",
                        null,
                        "user1@email.com"
                )
        );
        em.flush();

        Long workspaceParticipantId = workspace.findWorkspaceParticipantByMemberId(testMemberId).getId();
        projectService.inviteWorkspaceParticipant(
                workspaceId,
                projectId,
                new InviteWorkspaceParticipantRequestDto(workspaceParticipantId)
        );
        DeportProjectParticipantRequestDto requestDto = new DeportProjectParticipantRequestDto(workspaceParticipantId);

        // when
        projectService.deportProjectParticipant(projectId, requestDto);
        Project project = projectRepository.findProjectById(projectId).orElseThrow();

        // then
        assertEquals(1, project.getParticipants().size());
    }

    @DisplayName("프로젝트 탈퇴")
    @Test
    void withdrawProject() {
        // when
        projectService.withdrawProject(projectId);
        Project project = projectRepository.findProjectById(projectId).orElseThrow();

        // then
        assertEquals(0, project.getParticipants().size());
    }

    @DisplayName("프로젝트 삭제")
    @Test
    void deleteProject() {
        // when
        projectService.deleteProject(projectId);

        // then
        assertThrows(ProjectNotFoundException.class, () -> projectService.findProject(projectId));
    }
}