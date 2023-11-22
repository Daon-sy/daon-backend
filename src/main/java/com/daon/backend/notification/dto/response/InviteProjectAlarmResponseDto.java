package com.daon.backend.notification.dto.response;

import com.daon.backend.notification.dto.ProjectSummary;
import com.daon.backend.notification.dto.WorkspaceSummary;
import lombok.Getter;

@Getter
public class InviteProjectAlarmResponseDto {

    private WorkspaceSummary workspace;

    private ProjectSummary project;


    public InviteProjectAlarmResponseDto(Long workspaceId, String workspaceTitle, Long projectId, String projectTitle) {
        this.workspace = new WorkspaceSummary(workspaceId, workspaceTitle);
        this.project = new ProjectSummary(projectId, projectTitle);
    }
}
