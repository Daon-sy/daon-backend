package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectCreator;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.dto.request.CreateProjectRequestDto;
import com.daon.backend.task.dto.response.ProjectListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final WorkspaceRepository workspaceRepository;
    private final ProjectRepository projectRepository;
    private final SessionMemberProvider sessionMemberProvider;

    @Transactional
    public Long createProject(Long workspaceId, CreateProjectRequestDto requestDto) {
        Workspace workspace = getWorkspaceOrElseThrow(workspaceId);

        String memberId = sessionMemberProvider.getMemberId();
        WorkspaceParticipant wsParticipant = getWorkspaceParticipantOrElseThrow(workspace, memberId);

        Project project = Project.builder()
                .workspace(workspace)
                .title(requestDto.getProjectName())
                .description(requestDto.getProjectDescription())
                .projectCreator(new ProjectCreator(memberId, wsParticipant))
                .build();
        return projectRepository.save(project).getId();
    }

    // 해당 워크스페이스가 없다면 오류발생
    private Workspace getWorkspaceOrElseThrow(Long workspaceId) {
        return workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
    }

    // 워크스페이스 참여자가 아니라면 오류 발생
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
}
