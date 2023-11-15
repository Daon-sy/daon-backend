package com.daon.backend.task.dto.workspace;

import com.daon.backend.task.domain.workspace.Workspace;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FindWorkspaceResponseDto {

    private Long workspaceId;

    private String title;

    private String description;

    private String imageUrl;

    private String division;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public FindWorkspaceResponseDto(Workspace workspace) {
        this.workspaceId = workspace.getId();
        this.title = workspace.getTitle();
        this.description = workspace.getDescription();
        this.imageUrl = workspace.getImageUrl();
        this.division = String.valueOf(workspace.getDivision());
        this.createdAt = workspace.getCreatedAt();
        this.modifiedAt = workspace.getModifiedAt();
    }
}
