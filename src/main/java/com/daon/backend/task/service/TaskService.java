package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskBookmark;
import com.daon.backend.task.domain.task.TaskNotFoundException;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.dto.task.CreateTaskRequestDto;
import com.daon.backend.task.dto.task.ModifyProgressStatusRequestDto;
import com.daon.backend.task.dto.task.ModifyTaskRequestDto;
import com.daon.backend.task.dto.task.CreateTaskResponseDto;
import com.daon.backend.task.dto.task.FindTaskResponseDto;
import com.daon.backend.task.dto.task.FindTasksResponseDto;
import com.daon.backend.task.dto.task.SetBookmarkResponseDto;
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
                .orElseThrow(() -> new NotProjectParticipantException(memberId, projectId));
        ProjectParticipant taskManager = getProjectParticipantByProjectParticipantId(taskManagerId, project, memberId);
        Board board = project.getBoardByBoardId(requestDto.getBoardId());

        Task task = Task.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .emergency(requestDto.isEmergency())
                .progressStatus(requestDto.getProgressStatus())
                .creator(taskCreator)
                .taskManager(taskManager)
                .project(project)
                .board(board)
                .build();

        Long taskId = taskRepository.save(task).getId();
        return new CreateTaskResponseDto(taskId);
    }

    private ProjectParticipant getProjectParticipantByProjectParticipantId(Long taskManagerId, Project project, String memberId) {
        ProjectParticipant taskManager = null;
        if (taskManagerId != null) {
            taskManager = project.findProjectParticipantByProjectParticipantId(taskManagerId)
                    .orElseThrow(() -> new NotProjectParticipantException(memberId, project.getId()));
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

    public FindTasksResponseDto findAllTaskInProject(Long projectId) {
        Project project = getProjectOrElseThrow(projectId);
        List<Task> tasks = project.getTasks();

        return new FindTasksResponseDto(
                tasks.stream()
                        .map(FindTasksResponseDto.TaskSummary::new)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void modifyTask(Long projectId, Long taskId, ModifyTaskRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        Long taskManagerId = requestDto.getTaskManagerId();

        Project project = getProjectByProjectId(projectId);
        Board board = project.getBoardByBoardId(requestDto.getBoardId());
        Task task = project.getTaskByTaskId(taskId);
        ProjectParticipant taskManager = getProjectParticipantByProjectParticipantId(taskManagerId, project, memberId);

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

    public FindTaskResponseDto findTask(Long projectId, Long taskId) {
        Project findProject = getProjectByProjectId(projectId);
        Task task = findProject.getTaskByTaskId(taskId);

        return new FindTaskResponseDto(task);
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
        Project project = getProjectByProjectId(projectId);
        Task task = project.getTaskByTaskId(taskId);
        task.modifyProgressStatus(requestDto.getProgressStatus());
    }
}
