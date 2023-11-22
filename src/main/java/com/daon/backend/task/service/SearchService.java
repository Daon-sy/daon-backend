package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import com.daon.backend.task.dto.TaskSearchParams;
import com.daon.backend.task.dto.task.FindTasksResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final SessionMemberProvider sessionMemberProvider;
    private final WorkspaceRepository workspaceRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    /**
     * 할 일 목록 조회
     */
    public FindTasksResponseDto searchTasks(TaskSearchParams params) {
        return new FindTasksResponseDto(taskRepository.findTaskSummaries(sessionMemberProvider.getMemberId(), params));
    }

    /**
     * 통합 검색
     */
    public <T> Slice<T> integratedSearchByTitle(String target, String title, Pageable pageable) {
        String memberId = sessionMemberProvider.getMemberId();

        switch (target) {
            case "workspace":
                return (Slice<T>) workspaceRepository.searchWorkspaceSummariesByTitle(memberId, title, pageable);
            case "project":
                return (Slice<T>) projectRepository.searchProjectSummariesByTitle(memberId, title, pageable);
            case "task":
                return (Slice<T>) taskRepository.searchTaskSummariesByTitle(memberId, title, pageable);
            default:
                throw new InvalidTargetException(target);
        }
    }
}
