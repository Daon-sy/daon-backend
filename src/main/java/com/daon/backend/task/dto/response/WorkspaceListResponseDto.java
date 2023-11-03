package com.daon.backend.task.dto.response;

import com.daon.backend.task.domain.workspace.Workspace;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class WorkspaceListResponseDto {

    private int totalCount;
    private List<WorkspaceSummary> workspaces;

    public WorkspaceListResponseDto(List<WorkspaceSummary> workspaces) {
        this.totalCount = workspaces.size();
        this.workspaces = workspaces;
    }

    @Getter
    @AllArgsConstructor
    public static class WorkspaceSummary {

        private Long workspaceId;
        private String title;
        private String imageUrl;
        private String division;

        // 편의용 생성자
        public WorkspaceSummary(Workspace workspace) {
            this.workspaceId = workspace.getId();
            this.title = workspace.getTitle();
            this.imageUrl = workspace.getImageUrl();
            this.division = workspace.getDivision().name();
        }
    }
}
