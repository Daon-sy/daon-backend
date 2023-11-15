package com.daon.backend.task.dto.response;

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
}
