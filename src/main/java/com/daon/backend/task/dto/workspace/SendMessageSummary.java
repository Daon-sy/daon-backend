package com.daon.backend.task.dto.workspace;

import com.daon.backend.task.domain.workspace.Message;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.daon.backend.task.dto.WorkspaceParticipantProfile;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SendMessageSummary {

    private Long messageId;

    private String title;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private WorkspaceParticipantProfile receiver;

    private boolean readed;

    public SendMessageSummary(Message message, WorkspaceParticipant workspaceParticipant) {
        this.messageId = message.getId();
        this.title = message.getTitle();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
        if (workspaceParticipant != null) {
            this.receiver = new WorkspaceParticipantProfile(workspaceParticipant);
        } else {
            this.receiver = null;
        }
        this.readed = message.isReaded();
    }
}
