package com.daon.backend.task.dto.notification;

import lombok.Getter;

@Getter
public class DeportationWorkspaceAlarmResponseDto {

    private WorkspaceSummaryForNotification workspace;

    public DeportationWorkspaceAlarmResponseDto(Long workspaceId, String workspaceTitle) {
        this.workspace = new WorkspaceSummaryForNotification(workspaceId, workspaceTitle);
    }
}
