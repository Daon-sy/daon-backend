package com.daon.backend.notification.dto.response;

import com.daon.backend.notification.dto.ProjectSummary;
import com.daon.backend.notification.dto.TaskSummary;
import com.daon.backend.notification.dto.WorkspaceSummary;
import lombok.Getter;

@Getter
public class DesignatedManagerResponseDto {

    private WorkspaceSummary workspace;

    private ProjectSummary project;

    private TaskSummary task;

    public DesignatedManagerResponseDto(Long workspaceId, String workspaceTitle,
                                        Long projectId, String projectTitle,
                                        Long taskId, String taskTitle) {
        this.workspace = new WorkspaceSummary(workspaceId, workspaceTitle);
        this.project = new ProjectSummary(projectId, projectTitle);
        this.task = new TaskSummary(taskId, taskTitle);
    }
}
