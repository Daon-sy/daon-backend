package com.daon.backend.task.dto.project;

import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.workspace.Role;
import lombok.Getter;

import java.util.List;

@Getter
public class FindProjectParticipantsResponseDto {

    private int totalCount;

    private List<ProjectParticipantProfile> projectParticipants;

    public FindProjectParticipantsResponseDto(List<ProjectParticipantProfile> projectParticipants) {
        this.totalCount = projectParticipants.size();
        this.projectParticipants = projectParticipants;
    }

    @Getter
    public static class ProjectParticipantProfile {

        private Long participantId;

        private String name;

        private String email;

        private String imageUrl;

        private Role role;

        public ProjectParticipantProfile(ProjectParticipant projectParticipant) {
            this.participantId = projectParticipant.getId();
            this.name = projectParticipant.getWorkspaceParticipant().getProfile().getName();
            this.email = projectParticipant.getWorkspaceParticipant().getProfile().getEmail();
            this.imageUrl = projectParticipant.getWorkspaceParticipant().getProfile().getImageUrl();
            this.role = projectParticipant.getWorkspaceParticipant().getRole();
        }
    }

}
