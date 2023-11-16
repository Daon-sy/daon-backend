package com.daon.backend.task.dto.workspace;

import com.daon.backend.task.dto.WorkspaceSummary;
import lombok.Getter;

import java.util.List;

@Getter
public class FindWorkspacesResponseDto {

    private int totalCount;

    private List<WorkspaceSummary> workspaces;

    public FindWorkspacesResponseDto(List<WorkspaceSummary> workspaces) {
        this.totalCount = workspaces.size();
        this.workspaces = workspaces;
    }
}
