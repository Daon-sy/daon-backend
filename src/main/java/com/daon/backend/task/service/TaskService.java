package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.*;
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

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    private final SessionMemberProvider sessionMemberProvider;

    @Transactional
    public CreateTaskResponseDto createTask(Long projectId, CreateTaskRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();

        Project project = projectRepository.findProjectByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        ProjectParticipant taskCreator = project.findProjectParticipantByMemberId(memberId)
                .orElseThrow(() -> new NotProjectParticipantException(memberId, project.getId()));
        ProjectParticipant taskManager = project.findProjectParticipantByProjectParticipantId(requestDto.getTaskManagerId())
                .orElseThrow(() -> new NotProjectParticipantException(requestDto.getTaskManagerId(), project.getId()));
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