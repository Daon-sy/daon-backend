package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.dto.response.ProjectListResponseDto;
import com.daon.backend.task.dto.response.TaskListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final WorkspaceRepository workspaceRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final SessionMemberProvider sessionMemberProvider;


    // 해당 워크스페이스가 없다면 오류발생
    private Workspace getWorkspaceOrElseThrow(Long workspaceId) {
        return workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
    }

    //해당 프로젝트가 없다면 오류발생
    private Project getProjectOrElseThrow(Long projectId) {
        return projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    //해당 프로젝트의 참여자가 아니라면 오류발생
    private ProjectParticipant getProjectParticipantOrElseThrow(Project project,
                                                                String memberId) {
        return projectRepository.findProjectParticipantByProjectAndMemberId(project, memberId)
                .orElseThrow(() -> new NotProjectParticipantException(memberId, project.getId()));
    }

    //목록 리스트
    public TaskListResponseDto findAllTaskInProject(Long workspaceId, Long projectId) {
        Workspace workspace = getWorkspaceOrElseThrow(workspaceId);
        Project project = getProjectOrElseThrow(projectId);

        String memberId = sessionMemberProvider.getMemberId();
        ProjectParticipant pjParticipant = getProjectParticipantOrElseThrow(project, memberId);

        return TaskListResponseDto(
                workspace.getId(),
                project.getId(),
                taskRepository.findTaskByProjectParticipant(pjParticipant).stream()
                        .map(TaskListResponseDto.TaskSummary::new)
                        .collect(Collectors.toList())
        );
    }
}
