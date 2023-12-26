package com.daon.backend.task.dto;

import com.daon.backend.task.domain.workspace.Role;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@AllArgsConstructor
public class WorkspaceNoticeWriter {
    private Long workspaceParticipantId;
    private String name;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private Role role;

    public WorkspaceNoticeWriter(WorkspaceParticipant participant){
        this.workspaceParticipantId = participant.getId();
        this.name = participant.getProfile().getName();
        this.imageUrl = participant.getProfile().getImageUrl();
        this.role = participant.getRole();
    }
}
