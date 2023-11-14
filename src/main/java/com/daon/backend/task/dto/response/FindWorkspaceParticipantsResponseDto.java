package com.daon.backend.task.dto.response;

import com.daon.backend.task.domain.workspace.Role;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class FindWorkspaceParticipantsResponseDto {

    private int totalCount;

    private List<WorkspaceParticipantProfile> workspaceParticipants;

    public FindWorkspaceParticipantsResponseDto(List<WorkspaceParticipantProfile> workspaceParticipants) {
        this.totalCount = workspaceParticipants.size();
        this.workspaceParticipants = workspaceParticipants;
    }

    @Getter
    @AllArgsConstructor
    public static class WorkspaceParticipantProfile {

        private Long workspaceParticipantId;

        private String name;

        private String email;

        private String imageUrl;

        private Role role;


        public WorkspaceParticipantProfile(WorkspaceParticipant workspaceParticipant) {
            this.workspaceParticipantId = workspaceParticipant.getId();
            this.name = workspaceParticipant.getProfile().getName();
            this.imageUrl = workspaceParticipant.getProfile().getImageUrl();
            this.email = workspaceParticipant.getProfile().getEmail();
            this.role = workspaceParticipant.getRole();
        }
    }
}
