package com.daon.backend.notification.dto.response;

import com.daon.backend.notification.dto.WorkspaceSummary;
import lombok.Getter;

@Getter
public class DeportationWorkspaceResponseDto {

    private WorkspaceSummary workspace;

    public DeportationWorkspaceResponseDto(Long workspaceId, String workspaceTitle) {
        this.workspace = new WorkspaceSummary(workspaceId, workspaceTitle);
    }
}
