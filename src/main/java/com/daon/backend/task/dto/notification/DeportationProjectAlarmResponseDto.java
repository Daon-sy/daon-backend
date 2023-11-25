package com.daon.backend.task.dto.notification;

import lombok.Getter;

@Getter
public class DeportationProjectAlarmResponseDto {

    private WorkspaceSummaryForNotification workspace;

    private ProjectSummaryForNotification project;

    public DeportationProjectAlarmResponseDto(Long workspaceId, String workspaceTitle, Long projectId, String projectTitle) {
        this.workspace = new WorkspaceSummaryForNotification(workspaceId, workspaceTitle);
        this.project = new ProjectSummaryForNotification(projectId, projectTitle);
    }
}
