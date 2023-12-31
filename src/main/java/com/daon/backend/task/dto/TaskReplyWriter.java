package com.daon.backend.task.dto;

import com.daon.backend.task.domain.project.ProjectParticipant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskReplyWriter {

    private Long projectParticipantId;

    private String name;

    private String imageUrl;

    public TaskReplyWriter(ProjectParticipant participant) {
        this.projectParticipantId = participant.getId();
        this.name = participant.getWorkspaceParticipant().getProfile().getName();
        this.imageUrl = participant.getWorkspaceParticipant().getProfile().getImageUrl();
    }
}
