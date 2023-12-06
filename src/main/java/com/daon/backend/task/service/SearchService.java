package com.daon.backend.task.service;

import com.daon.backend.common.response.slice.SliceResponse;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final SessionMemberProvider sessionMemberProvider;
    private final WorkspaceRepository workspaceRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;



    /**
     * 통합 검색
     */
    public <T> SliceResponse<T> integratedSearchByTitle(String target, String title, Pageable pageable) {
        String memberId = sessionMemberProvider.getMemberId();

        switch (target) {
            case "workspace":
                return new SliceResponse(workspaceRepository.searchWorkspaceSummariesByTitle(memberId, title, pageable));
            case "project":
                return new SliceResponse(projectRepository.searchProjectSummariesByTitle(memberId, title, pageable));
            case "task":
                return new SliceResponse(taskRepository.searchTaskSummariesByTitle(memberId, title, pageable));
            default:
                throw new InvalidTargetException(target);
        }
    }
}
