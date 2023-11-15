package com.daon.backend.task.dto;

import com.daon.backend.task.domain.project.ProjectParticipant;
import lombok.Getter;

@Getter
public class TaskManager {

    private Long projectParticipantId;

    private String name;

    private String imageUrl;

    public TaskManager(ProjectParticipant participant) {
        this.projectParticipantId = participant.getId();
        this.name = participant.getWorkspaceParticipant().getProfile().getName();
        this.imageUrl = participant.getWorkspaceParticipant().getProfile().getImageUrl();
    }
}
