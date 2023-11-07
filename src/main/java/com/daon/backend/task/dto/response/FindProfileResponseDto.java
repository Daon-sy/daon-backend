package com.daon.backend.task.dto.response;

import com.daon.backend.task.domain.workspace.Role;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.Getter;

@Getter
public class FindProfileResponseDto {

    private Long participantId;
    private String name;
    private String imageUrl;
    private String email;
    private String roleDescription;

    public FindProfileResponseDto(WorkspaceParticipant workspaceParticipant) {
        this.participantId = workspaceParticipant.getId();
        this.name = workspaceParticipant.getProfile().getName();
        this.imageUrl = workspaceParticipant.getProfile().getImageUrl();
        this.email = workspaceParticipant.getProfile().getEmail();
        this.roleDescription = workspaceParticipant.getRole().getRoleDescription();
    }
}
