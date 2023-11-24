package com.daon.backend.task.dto.notification;

import lombok.Getter;

@Getter
public class InviteWorkspaceAlarmResponseDto {

    private WorkspaceSummaryForNotification workspace;

    public InviteWorkspaceAlarmResponseDto(Long workspaceId, String workspaceTitle) {
        this.workspace = new WorkspaceSummaryForNotification(workspaceId, workspaceTitle);
    }
}