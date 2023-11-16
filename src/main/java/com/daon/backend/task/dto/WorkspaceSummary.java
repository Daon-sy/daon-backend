package com.daon.backend.task.dto;

import com.daon.backend.task.domain.workspace.Division;
import com.daon.backend.task.domain.workspace.Workspace;
import lombok.Getter;

@Getter
public class WorkspaceSummary {

    private Long workspaceId;

    private String title;

    private String imageUrl;

    private Division division;

    private String description;

    public WorkspaceSummary(Long workspaceId, String title, String imageUrl, Division division, String description) {
        this.workspaceId = workspaceId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.division = division;
        this.description = description;
    }

    public WorkspaceSummary(Workspace workspace) {
        this.workspaceId = workspace.getId();
        this.title = workspace.getTitle();
        this.imageUrl = workspace.getImageUrl();
        this.division = workspace.getDivision();
        this.description = workspace.getDescription();
    }
}
