package com.daon.backend.task.dto.response;

import com.daon.backend.task.domain.project.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProjectListResponseDto {

    private Long workspaceId;
    private int totalCount;
    private List<ProjectSummary> projects;


    public ProjectListResponseDto(Long workspaceId, List<ProjectSummary> projects) {
        this.workspaceId = workspaceId;
        this.totalCount = projects.size();
        this.projects = projects;
    }

    @Getter
    @AllArgsConstructor
    public static class ProjectSummary {

        private Long projectId;
        private String projectName;
        private String description;

        public ProjectSummary(Project project) {
            this.projectId = project.getId();
            this.projectName = project.getTitle();
            this.description = project.getDescription();
        }
    }
}
