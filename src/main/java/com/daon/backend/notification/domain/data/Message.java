package com.daon.backend.notification.domain.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    private Long messageId;

    private Long workspaceParticipantId;

    private String name;

    private String email;

    private String imageUrl;

    public Message(Long messageId, Long workspaceParticipantId, String name, String email, String imageUrl) {
        this.messageId = messageId;
        this.workspaceParticipantId = workspaceParticipantId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }
}
