package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.domain.workspace.*;
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
    private final TaskRepository taskRepository;
    private final SessionMemberProvider sessionMemberProvider;

    //해당 프로젝트가 없다면 오류발생
    private Project getProjectOrElseThrow(Long projectId) {
        return projectRepository.findProjectWithParticipantsById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    //목록 리스트
    public TaskListResponseDto findAllTaskInProject(Long workspaceId, Long projectId) {
        Project project = getProjectOrElseThrow(projectId);
        List<Task> tasks = project.getTasks();

        return new TaskListResponseDto(
                tasks.stream()
                        .map(TaskListResponseDto.TaskSummary::new)
                        .collect(Collectors.toList())
        );
    }
}
