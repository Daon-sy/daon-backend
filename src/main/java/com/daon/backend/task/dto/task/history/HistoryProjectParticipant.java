package com.daon.backend.task.dto.task.history;

import lombok.Getter;

@Getter
public class HistoryProjectParticipant {

    private Long projectParticipantId;
    private String name;
    private String imageUrl;

    public HistoryProjectParticipant(Long projectParticipantId, String name, String imageUrl) {
        this.projectParticipantId = projectParticipantId;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
