package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskBookmark;
import com.daon.backend.task.domain.task.TaskNotFoundException;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.dto.task.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        Project project = projectRepository.findProjectByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        ProjectParticipant projectParticipant = project.findProjectParticipantByMemberId(memberId)
                .orElseThrow(() -> new NotProjectParticipantException(memberId, projectId));

        ProjectParticipant taskManager = null;
        if (taskManagerId != null) {
            taskManager = projectParticipant;
        }

        Board board = project.getBoardByBoardId(requestDto.getBoardId());

        Task task = Task.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .emergency(requestDto.isEmergency())
                .creatorId(projectParticipant.getWorkspaceParticipant().getId())
                .taskManager(taskManager)
                .project(project)
                .board(board)
                .build();

        Long taskId = taskRepository.save(task).getId();
        return new CreateTaskResponseDto(taskId);
    }

    public FindTaskResponseDto findTask(Long taskId) {
        return new FindTaskResponseDto(
                taskRepository.findTaskDetail(sessionMemberProvider.getMemberId(), taskId)
                        .orElseThrow(() -> new TaskNotFoundException(taskId))
        );
    }

    @Transactional
    public void modifyTask(Long projectId, Long taskId, ModifyTaskRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        Long taskManagerId = requestDto.getTaskManagerId();

        Project project = projectRepository.findProjectByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        Board board = project.getBoardByBoardId(requestDto.getBoardId());
        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(projectId, taskId));

        ProjectParticipant taskManager = null;
        if (taskManagerId != null) {
            taskManager = project.findProjectParticipantByProjectParticipantId(taskManagerId)
                    .orElseThrow(() -> new NotProjectParticipantException(memberId, project.getId()));
        }

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
    public SetBookmarkResponseDto setBookmark(Long projectId, Long taskId) {
        String memberId = sessionMemberProvider.getMemberId();
        boolean created;

        ProjectParticipant projectParticipant =
                projectRepository.findProjectParticipantByProjectIdAndMemberId(projectId, memberId)
                        .orElseThrow(() -> new NotProjectParticipantException(memberId, projectId));
        Long projectParticipantId = projectParticipant.getId();

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
        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(projectId, taskId));
        task.modifyProgressStatus(requestDto.getProgressStatus());
    }

    public void deleteTask(Long taskId) {
        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        task.removeTask();
    }
}
