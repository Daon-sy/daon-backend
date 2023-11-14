package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.dto.request.CreateProjectRequestDto;
import com.daon.backend.task.dto.request.InviteWorkspaceParticipantRequestDto;
import com.daon.backend.task.dto.response.CreateProjectResponseDto;
import com.daon.backend.task.dto.response.FindProjectParticipantsResponseDto;
import com.daon.backend.task.dto.response.ProjectListResponseDto;
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

    @Transactional
    public CreateProjectResponseDto createProject(Long workspaceId, CreateProjectRequestDto requestDto) {
        Workspace workspace = getWorkspaceOrElseThrow(workspaceId);

        String memberId = sessionMemberProvider.getMemberId();
        WorkspaceParticipant wsParticipant = getWorkspaceParticipantOrElseThrow(workspace, memberId);

        Project project = Project.builder()
                .workspace(workspace)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .projectCreator(new ProjectCreator(memberId, wsParticipant))
                .build();
        Long projectId = projectRepository.save(project).getId();

        return new CreateProjectResponseDto(projectId);
    }

    private Workspace getWorkspaceOrElseThrow(Long workspaceId) {
        return workspaceRepository.findWorkspaceByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
    }

    private WorkspaceParticipant getWorkspaceParticipantOrElseThrow(Workspace workspace,
                                                                    String memberId) {
        return workspaceRepository.findWorkspaceParticipantByWorkspaceAndMemberId(workspace, memberId)
                .orElseThrow(() -> new NotWorkspaceParticipantException(memberId, workspace.getId()));
    }

    public ProjectListResponseDto findAllProjectInWorkspace(Long workspaceId) {
        Workspace workspace = getWorkspaceOrElseThrow(workspaceId);

        String memberId = sessionMemberProvider.getMemberId();
        WorkspaceParticipant wsParticipant = getWorkspaceParticipantOrElseThrow(workspace, memberId);

        return new ProjectListResponseDto(
                workspace.getId(),
                projectRepository.findProjectsByWorkspaceParticipant(wsParticipant).stream()
                        .map(ProjectListResponseDto.ProjectSummary::new)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void inviteWorkspaceParticipant(Long projectId, InviteWorkspaceParticipantRequestDto requestDto) {
        Long workspaceParticipantId = requestDto.getWorkspaceParticipantId();
        WorkspaceParticipant workspaceParticipant = workspaceRepository.findWorkspaceParticipantByWorkspaceParticipantId(workspaceParticipantId)
                .orElseThrow(() -> new NotWorkspaceParticipantException(workspaceParticipantId));

        Project findProject = projectRepository.findProjectByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        findProject.addParticipant(workspaceParticipant.getMemberId(), workspaceParticipant);
    }

    public boolean isProjectParticipants(Long projectId, String memberId) {
        Project findProject = projectRepository.findProjectWithParticipantsById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return findProject.isProjectParticipants(memberId);
    }

    public FindProjectParticipantsResponseDto findProjectParticipants(Long projectId) {
        List<ProjectParticipant> participants = projectRepository.findProjectParticipantsWithWorkspaceParticipantsByProjectId(projectId);

        return new FindProjectParticipantsResponseDto(
                participants.stream()
                        .map(FindProjectParticipantsResponseDto.ProjectParticipantProfile::new)
                        .collect(Collectors.toList())
        );
    }
}
