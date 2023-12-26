package com.daon.backend.task.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InviteWorkspaceAlarmResponseDto {

    private WorkspaceSummaryForNotification workspace;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime time;

    public InviteWorkspaceAlarmResponseDto(Long workspaceId, String workspaceTitle) {
        this.workspace = new WorkspaceSummaryForNotification(workspaceId, workspaceTitle);
        this.time = LocalDateTime.now();
    }
}