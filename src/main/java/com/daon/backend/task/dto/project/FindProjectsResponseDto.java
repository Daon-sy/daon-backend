package com.daon.backend.task.dto.project;

import com.daon.backend.task.dto.ProjectSummary;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindProjectsResponseDto {

    private Long workspaceId;

    private int totalCount;

    private List<ProjectSummary> projects;


    public FindProjectsResponseDto(Long workspaceId, List<ProjectSummary> projects) {
        this.workspaceId = workspaceId;
        this.totalCount = projects.size();
        this.projects = projects;
    }
}
