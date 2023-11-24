package com.daon.backend.task.dto.notification;

import lombok.Getter;

@Getter
public class DesignatedManagerAlarmResponseDto {

    private WorkspaceSummaryForNotification workspace;

    private ProjectSummaryForNotification project;

    private TaskSummaryForNotification task;

    public DesignatedManagerAlarmResponseDto(Long workspaceId, String workspaceTitle,
                                             Long projectId, String projectTitle,
                                             Long taskId, String taskTitle) {
        this.workspace = new WorkspaceSummaryForNotification(workspaceId, workspaceTitle);
        this.project = new ProjectSummaryForNotification(projectId, projectTitle);
        this.task = new TaskSummaryForNotification(taskId, taskTitle);
    }
}
