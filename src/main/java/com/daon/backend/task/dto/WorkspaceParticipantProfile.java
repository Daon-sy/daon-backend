package com.daon.backend.task.dto;

import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.Getter;

@Getter
public class WorkspaceParticipantProfile {

    private Long workspaceParticipantId;

    private String name;

    private String email;

    private String imageUrl;

    public WorkspaceParticipantProfile(WorkspaceParticipant workspaceParticipant) {
        this.workspaceParticipantId = workspaceParticipant.getId();
        this.name = workspaceParticipant.getProfile().getName();
        this.email = workspaceParticipant.getProfile().getEmail();
        this.imageUrl = workspaceParticipant.getProfile().getImageUrl();
    }
}
