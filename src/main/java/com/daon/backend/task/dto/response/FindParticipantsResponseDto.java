package com.daon.backend.task.dto.response;

import com.daon.backend.task.domain.workspace.Role;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FindParticipantsResponseDto {

    private int totalCount;

    private List<ParticipantProfile> participants;

    public FindParticipantsResponseDto(List<ParticipantProfile> participants) {
        this.totalCount = participants.size();
        this.participants = participants;
    }

    @Getter
    public static class ParticipantProfile {

        private Long participantId;

        private String name;

        private String email;

        private String imageUrl;

        private String roleDescription;


        public ParticipantProfile(WorkspaceParticipant workspaceParticipant) {
            this.participantId = workspaceParticipant.getId();
            this.name = workspaceParticipant.getProfile().getName();
            this.imageUrl = workspaceParticipant.getProfile().getImageUrl();
            this.email = workspaceParticipant.getProfile().getEmail();
            this.roleDescription = workspaceParticipant.getRole().getRoleDescription();
        }
    }
}
