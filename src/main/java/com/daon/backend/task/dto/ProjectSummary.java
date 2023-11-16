package com.daon.backend.task.dto;

import com.daon.backend.task.domain.project.Project;
import lombok.Getter;

@Getter
public class ProjectSummary {

    private Long projectId;

    private String title;

    private String description;

    public ProjectSummary(Long projectId, String title, String description) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
    }

    public ProjectSummary(Project project) {
        this.projectId = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
    }
}
