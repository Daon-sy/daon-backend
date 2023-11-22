package com.daon.backend.notification.dto.response;

import com.daon.backend.notification.dto.WorkspaceSummary;
import lombok.Getter;

@Getter
public class InviteWorkspaceAlarmResponseDto {

    private WorkspaceSummary workspace;

    public InviteWorkspaceAlarmResponseDto(Long workspaceId, String workspaceTitle) {
        this.workspace = new WorkspaceSummary(workspaceId, workspaceTitle);
    }
}
