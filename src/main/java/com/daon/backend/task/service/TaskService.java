package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.dto.request.CreateTaskRequestDto;
import com.daon.backend.task.dto.request.ModifyProgressStatusRequestDto;
import com.daon.backend.task.dto.request.ModifyTaskRequestDto;
import com.daon.backend.task.dto.request.SetBookmarkRequestDto;
import com.daon.backend.task.dto.response.CreateTaskResponseDto;
import com.daon.backend.task.dto.response.SetBookmarkResponseDto;
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

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final SessionMemberProvider sessionMemberProvider;

    @Transactional
    public CreateTaskResponseDto createTask(Long projectId, CreateTaskRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        Long taskManagerId = requestDto.getTaskManagerId();

        Project project = getProjectByProjectId(projectId);
        ProjectParticipant taskCreator = project.findProjectParticipantByMemberId(memberId)
                .orElseThrow(() -> new NotProjectParticipantException(memberId, project.getId()));
        ProjectParticipant taskManager = getProjectParticipantByProjectParticipantId(taskManagerId, project);
        Board board = project.getBoardByBoardId(requestDto.getBoardId());

        Task task = Task.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .emergency(requestDto.isEmergency())
                .creator(taskCreator)
                .taskManager(taskManager)
                .project(project)
                .board(board)
                .build();

        Long taskId = taskRepository.save(task).getId();
        return new CreateTaskResponseDto(taskId);
    }

    private ProjectParticipant getProjectParticipantByProjectParticipantId(Long taskManagerId, Project project) {
        ProjectParticipant taskManager = null;
        if (taskManagerId != null) {
            taskManager = project.findProjectParticipantByProjectParticipantId(taskManagerId)
                    .orElseThrow(() -> new NotProjectParticipantException(taskManagerId, project.getId()));
        }
        return taskManager;
    }

    private Project getProjectByProjectId(Long projectId) {
        return projectRepository.findProjectByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    private Project getProjectOrElseThrow(Long projectId) {
        return projectRepository.findProjectWithParticipantsById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    public TaskListResponseDto findAllTaskInProject(Long projectId) {
        Project project = getProjectOrElseThrow(projectId);
        List<Task> tasks = project.getTasks();

        return new TaskListResponseDto(
                tasks.stream()
                        .map(TaskListResponseDto.TaskSummary::new)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void modifyTask(Long projectId, Long taskId, ModifyTaskRequestDto requestDto) {
        Long taskManagerId = requestDto.getTaskManagerId();

        Project project = getProjectByProjectId(projectId);
        Board board = project.getBoardByBoardId(requestDto.getBoardId());
        Task task = project.getTaskByTaskId(taskId);
        ProjectParticipant taskManager = getProjectParticipantByProjectParticipantId(taskManagerId, project);

        task.modifyTask(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getStartDate(),
                requestDto.getEndDate(),
                requestDto.isEmergency(),
                requestDto.getProgressStatus(),
                board,
                taskManager
        );
    }

    @Transactional
    public SetBookmarkResponseDto setBookmark(Long projectId, Long taskId, SetBookmarkRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        Long projectParticipantId = requestDto.getProjectParticipantId();
        boolean created;

        ProjectParticipant projectParticipant =
                projectRepository.findProjectParticipantByProjectParticipantId(projectParticipantId)
                        .orElseThrow(() -> new NotProjectParticipantException(projectParticipantId, projectId));
        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        boolean isBookmarked = taskRepository.existsTaskBookmarkByTaskIdAndProjectParticipantId(taskId, projectParticipantId);
        if (isBookmarked) {
            task.removeTaskBookmark(projectParticipant);
            created = false;
        } else {
            task.addTaskBookmark(new TaskBookmark(task, projectParticipant, memberId));
            created = true;
        }

        return new SetBookmarkResponseDto(created);
    }

    @Transactional
    public void modifyTaskProgressStatus(Long projectId, Long taskId, ModifyProgressStatusRequestDto requestDto) {
        Project project = getProjectByProjectId(projectId);
        Task task = project.getTaskByTaskId(taskId);
        task.modifyProgressStatus(requestDto.getProgressStatus());
    }
}
