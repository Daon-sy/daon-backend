package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceNotFoundException;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import com.daon.backend.task.dto.ProjectSummary;
import com.daon.backend.task.dto.project.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final WorkspaceRepository workspaceRepository;
    private final ProjectRepository projectRepository;
    private final SessionMemberProvider sessionMemberProvider;

    /**
     * 프로젝트 생성
     */
    @Transactional
    public CreateProjectResponseDto createProject(Long workspaceId, CreateProjectRequestDto requestDto) {
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        String memberId = sessionMemberProvider.getMemberId();
        WorkspaceParticipant workspaceParticipant = workspace.findWorkspaceParticipantByMemberId(memberId);

        Project project = Project.builder()
                .workspace(workspace)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .projectCreator(new ProjectCreator(memberId, workspaceParticipant))
                .build();

        return new CreateProjectResponseDto(projectRepository.save(project).getId());
    }

    /**
     * 프로젝트 목록 조회
     */
    public FindProjectsResponseDto findProjects(Long workspaceId) {
        String memberId = sessionMemberProvider.getMemberId();
        List<Project> projects = projectRepository.findProjectsByMemberIdOrderByDesc(workspaceId, memberId);

        return new FindProjectsResponseDto(
                workspaceId,
                projects.stream()
                        .map(ProjectSummary::new)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 프로젝트 초대
     */
    @Transactional
    public void inviteWorkspaceParticipant(Long workspaceId, Long projectId, InviteWorkspaceParticipantRequestDto requestDto) {
        Long workspaceParticipantId = requestDto.getWorkspaceParticipantId();
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        WorkspaceParticipant workspaceParticipant =
                workspace.findWorkspaceParticipantByWorkspaceParticipantId(workspaceParticipantId, workspaceId);
        Project project = workspace.findProject(projectId);
        project.addParticipant(workspaceParticipant.getMemberId(), workspaceParticipant);
    }

    public boolean isProjectParticipants(Long projectId, String memberId) {
        Project project = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return project.isProjectParticipants(memberId);
    }

    /**
     * 프로젝트 참여자 목록 조회
     */
    public FindProjectParticipantsResponseDto findProjectParticipants(Long projectId) {
        List<ProjectParticipant> participants = projectRepository.findProjectParticipantsByProjectId(projectId);

        return new FindProjectParticipantsResponseDto(
                participants.stream()
                        .map(FindProjectParticipantsResponseDto.ProjectParticipantProfile::new)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 프로젝트 수정
     */
    @Transactional
    public void modifyProject(Long projectId, ModifyProjectRequestDto requestDto) {
        Project project = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        project.modifyProject(requestDto.getTitle(), requestDto.getDescription());
    }

    /**
     * 프로젝트 단 건 조회
     */
    public FindProjectResponseDto findProject(Long projectId) {
        Project project = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return new FindProjectResponseDto(project);
    }

    /**
     * 프로젝트 탈퇴
     */
    @Transactional
    public void withdrawProject(Long projectId) {
        String memberId = sessionMemberProvider.getMemberId();
        Project project = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        projectRepository.deleteTaskManagerRelatedProjectByMemberId(projectId, memberId);
        project.withdrawProject(memberId);
    }

    /**
     * 프로젝트 참여자 강퇴
     */
    @Transactional
    public void deportProjectParticipant(Long projectId, DeportProjectParticipantRequestDto requestDto) {
        Long projectParticipantId = requestDto.getProjectParticipantId();
        Project project = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        projectRepository.deleteTaskManagerByProjectParticipantId(projectParticipantId);
        project.deportProject(projectParticipantId);
    }

    /**
     * 프로젝트 삭제
     */
    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        projectRepository.deleteTasksAndBoardsRelatedProject(projectId);
        project.deleteProject();
    }
}
