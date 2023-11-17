package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.domain.workspace.*;
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
    private final TaskRepository taskRepository;

    @Transactional
    public CreateProjectResponseDto createProject(Long workspaceId, CreateProjectRequestDto requestDto) {
        Workspace workspace = getWorkspaceOrElseThrow(workspaceId);

        String memberId = sessionMemberProvider.getMemberId();
        WorkspaceParticipant wsParticipant = getWorkspaceParticipantOrElseThrow(workspace.getId(), memberId);

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

    private WorkspaceParticipant getWorkspaceParticipantOrElseThrow(Long workspaceId,
                                                                    String memberId) {
        return workspaceRepository.findWorkspaceParticipantByWorkspaceIdAndMemberId(workspaceId, memberId)
                .orElseThrow(() -> new NotWorkspaceParticipantException(memberId, workspaceId));
    }

    public FindProjectsResponseDto findAllProjectInWorkspace(Long workspaceId) {
        Workspace workspace = getWorkspaceOrElseThrow(workspaceId);

        String memberId = sessionMemberProvider.getMemberId();
        WorkspaceParticipant wsParticipant = getWorkspaceParticipantOrElseThrow(workspaceId, memberId);

        return new FindProjectsResponseDto(
                workspace.getId(),
                projectRepository.findProjectsByWorkspaceParticipant(wsParticipant).stream()
                        .map(ProjectSummary::new)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void inviteWorkspaceParticipant(Long projectId, InviteWorkspaceParticipantRequestDto requestDto) {
        Long workspaceParticipantId = requestDto.getWorkspaceParticipantId();
        WorkspaceParticipant workspaceParticipant = workspaceRepository.findWorkspaceParticipantByWorkspaceParticipantId(workspaceParticipantId)
                .orElseThrow(() -> new NotWorkspaceParticipantException(workspaceParticipantId));

        Project project = projectRepository.findProjectByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        project.addParticipant(workspaceParticipant.getMemberId(), workspaceParticipant);
    }

    public boolean isProjectParticipants(Long projectId, String memberId) {
        Project project = projectRepository.findProjectWithParticipantsById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return project.isProjectParticipants(memberId);
    }

    public FindProjectParticipantsResponseDto findProjectParticipants(Long projectId) {
        List<ProjectParticipant> participants = projectRepository.findProjectParticipantsWithWorkspaceParticipantsByProjectId(projectId);

        return new FindProjectParticipantsResponseDto(
                participants.stream()
                        .map(FindProjectParticipantsResponseDto.ProjectParticipantProfile::new)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void modifyProject(Long projectId, ModifyProjectRequestDto requestDto) {
        Project project = projectRepository.findProjectWithParticipantsById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        project.modifyProject(requestDto.getTitle(), requestDto.getDescription());
    }

    public FindProjectResponseDto findProject(Long projectId) {
        Project project = projectRepository.findProjectByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return new FindProjectResponseDto(project);
    }

    /**
     * 프로젝트 탈퇴
     */
    @Transactional
    public void withdrawProject(Long projectId) {
        String memberId = sessionMemberProvider.getMemberId();

        List<Task> tasks = taskRepository.findTasksByProjectId(projectId);
        tasks.stream()
                .filter(task -> task.getTaskManager().getMemberId().equals(memberId))
                .forEach(Task::removeTaskManager);

        Project project = projectRepository.findProjectWithParticipantsById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        project.withdrawProject(memberId);
    }

    /**
     * 프로젝트 참여자 강퇴
     */
    @Transactional
    public void deportProjectParticipant(Long projectId, DeportProjectParticipantRequestDto requestDto) {
        Long projectParticipantId = requestDto.getProjectParticipantId();

        List<Task> tasks = taskRepository.findTasksByProjectId(projectId);
        tasks.stream()
                .filter(task -> task.getTaskManager().getId().equals(projectParticipantId))
                .forEach(Task::removeTaskManager);

        Project project = projectRepository.findProjectWithParticipantsById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        project.deportProject(projectParticipantId);
    }

    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findProjectWithParticipantsById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        List<Task> tasks = taskRepository.findTasksByProjectId(projectId);
        tasks.forEach(Task::removeTask);

        project.removeProject();
    }
}
