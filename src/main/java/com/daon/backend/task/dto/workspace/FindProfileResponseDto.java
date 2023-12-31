package com.daon.backend.task.dto.workspace;

import com.daon.backend.task.domain.workspace.Role;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindProfileResponseDto {

    private Long workspaceParticipantId;

    private String name;

    private String imageUrl;

    private String email;

    private Role role;

    public FindProfileResponseDto(WorkspaceParticipant workspaceParticipant) {
        this.workspaceParticipantId = workspaceParticipant.getId();
        this.name = workspaceParticipant.getProfile().getName();
        this.imageUrl = workspaceParticipant.getProfile().getImageUrl();
        this.email = workspaceParticipant.getProfile().getEmail();
        this.role = workspaceParticipant.getRole();
    }
}
