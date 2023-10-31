package com.daon.backend.task.dto;

import com.daon.backend.task.domain.Workspace;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkspaceRequestDto {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private String subject;

    public Workspace toEntity() {
        return Workspace.builder()
                .id(id)
                .name(name)
                .imageUrl(imageUrl)
                .description(description)
                .subject(subject)
                .build();
    }
}
