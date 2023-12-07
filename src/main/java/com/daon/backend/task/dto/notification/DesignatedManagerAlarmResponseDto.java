package com.daon.backend.task.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DesignatedManagerAlarmResponseDto {

    private WorkspaceSummaryForNotification workspace;

    private ProjectSummaryForNotification project;

    private TaskSummaryForNotification task;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime time;

    public DesignatedManagerAlarmResponseDto(Long workspaceId, String workspaceTitle,
                                             Long projectId, String projectTitle,
                                             Long taskId, String taskTitle) {
        this.workspace = new WorkspaceSummaryForNotification(workspaceId, workspaceTitle);
        this.project = new ProjectSummaryForNotification(projectId, projectTitle);
        this.task = new TaskSummaryForNotification(taskId, taskTitle);
        this.time = LocalDateTime.now();
    }
}
