package com.daon.backend.task.dto.notification;

import lombok.Getter;

@Getter
public class InviteProjectAlarmResponseDto {

    private WorkspaceSummaryForNotification workspace;

    private ProjectSummaryForNotification project;

    public InviteProjectAlarmResponseDto(Long workspaceId, String workspaceTitle, Long projectId, String projectTitle) {
        this.workspace = new WorkspaceSummaryForNotification(workspaceId, workspaceTitle);
        this.project = new ProjectSummaryForNotification(projectId, projectTitle);
    }
}
