package com.daon.backend.task.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeportationProjectAlarmResponseDto {

    private WorkspaceSummaryForNotification workspace;

    private ProjectSummaryForNotification project;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime time;

    public DeportationProjectAlarmResponseDto(Long workspaceId, String workspaceTitle, Long projectId, String projectTitle) {
        this.workspace = new WorkspaceSummaryForNotification(workspaceId, workspaceTitle);
        this.project = new ProjectSummaryForNotification(projectId, projectTitle);
        this.time = LocalDateTime.now();
    }
}
