package com.daon.backend.notification.dto.response;

import com.daon.backend.notification.dto.ProjectSummary;
import com.daon.backend.notification.dto.WorkspaceSummary;
import lombok.Getter;

@Getter
public class DeportationProjectResponseDto {

    private WorkspaceSummary workspace;

    private ProjectSummary project;

    public DeportationProjectResponseDto(Long workspaceId, String workspaceTitle, Long projectId, String projectTitle) {
        this.workspace = new WorkspaceSummary(workspaceId, workspaceTitle);
        this.project = new ProjectSummary(projectId, projectTitle);
    }
}
