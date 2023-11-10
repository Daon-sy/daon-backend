package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceNotFoundException;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import com.daon.backend.task.dto.request.CreateTaskRequestDto;
import com.daon.backend.task.dto.response.CreateTaskResponseDto;
import com.daon.backend.task.dto.response.TaskListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final WorkspaceRepository workspaceRepository;
    private final ProjectRepository projectRepository;
    private final BoardRepository boardRepository;
    private final TaskRepository taskRepository;
    private final SessionMemberProvider sessionMemberProvider;

    @Transactional
    public CreateTaskResponseDto createTask(Long workspaceId, Long projectId, CreateTaskRequestDto requestDto) {
        /**
         * 주어진 정보 : 프로젝트 아이디, 워크스페이스 아이디, 요청 dto, memberId
         */
        String memberId = sessionMemberProvider.getMemberId();
        Workspace workspace = getWorkspaceOrElseThrow(workspaceId);
        Project project = getProjectOrElseThrow(projectId);
        ProjectParticipant projectParticipant = projectRepository.findProjectParticipantByProjectAndMemberId(project, memberId)
                .orElseThrow();
        WorkspaceParticipant workspaceParticipant = workspaceRepository.findWorkspaceParticipantByWorkspaceAndMemberId(workspace, memberId)
                .orElseThrow();
        Board board = boardRepository.findBoardByProjectId(projectId)
                .orElseThrow();

        Task task = Task.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .emergency(requestDto.isEmergency())
                .progressStatus(TaskProgressStatus.TODO)
                .creator(projectParticipant)
                .taskManager(workspaceParticipant)
                .project(project)
                .board(board)
                .build();

        Long taskId = taskRepository.save(task).getId();
        return new CreateTaskResponseDto(taskId);
    }

    // 해당 워크스페이스가 없다면 오류발생
    private Workspace getWorkspaceOrElseThrow(Long workspaceId) {
        return workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
    }

    //해당 프로젝트가 없다면 오류발생
    private Project getProjectOrElseThrow(Long projectId) {
        return projectRepository.findProjectWithParticipantsById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    //목록 리스트
    public TaskListResponseDto findAllTaskInProject(Long projectId) {
        Project project = getProjectOrElseThrow(projectId);
        List<Task> tasks = project.getTasks();

        return new TaskListResponseDto(
                tasks.stream()
                        .map(TaskListResponseDto.TaskSummary::new)
                        .collect(Collectors.toList())
        );
    }
}
