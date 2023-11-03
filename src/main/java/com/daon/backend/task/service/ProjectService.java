package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectCreator;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.dto.request.CreateProjectRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final WorkspaceRepository workspaceRepository;
    private final ProjectRepository projectRepository;
    private final SessionMemberProvider sessionMemberProvider;

    @Transactional
    public Long createProject(Long workspaceId, CreateProjectRequestDto requestDto) {
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        String memberId = sessionMemberProvider.getMemberId();
        // 워크스페이스 참여자인지 확인
        WorkspaceParticipant wsParticipant = workspaceRepository.findWorkspaceParticipantByWorkspaceAndMemberId(workspace, memberId)
                .orElseThrow(() -> new NotWorkspaceParticipantException(memberId, workspaceId));

        Project project = Project.builder()
                .workspace(workspace)
                .title(requestDto.getProjectName())
                .description(requestDto.getProjectDescription())
                .projectCreator(new ProjectCreator(memberId, wsParticipant))
                .build();
        return projectRepository.save(project).getId();
    }

}
