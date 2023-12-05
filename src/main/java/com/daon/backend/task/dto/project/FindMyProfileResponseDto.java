package com.daon.backend.task.dto.project;

import com.daon.backend.task.domain.project.ProjectParticipant;
import lombok.Getter;

@Getter
public class FindMyProfileResponseDto {

    private Long projectParticipantId;

    private String name;

    private String imageUrl;

    private String email;

    public FindMyProfileResponseDto(ProjectParticipant projectParticipant) {
        this.projectParticipantId = projectParticipant.getId();
        this.name = projectParticipant.getWorkspaceParticipant().getProfile().getName();
        this.imageUrl = projectParticipant.getWorkspaceParticipant().getProfile().getImageUrl();
        this.email = projectParticipant.getWorkspaceParticipant().getProfile().getEmail();
    }
}
