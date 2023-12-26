package com.daon.backend.task.dto.workspace;

import com.daon.backend.task.domain.workspace.Message;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.daon.backend.task.dto.WorkspaceParticipantProfile;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FindMessageResponseDto {

    private Long messageId;

    private String title;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private WorkspaceParticipantProfile sender;

    public FindMessageResponseDto(Message message, WorkspaceParticipant workspaceParticipant) {
        this.messageId = message.getId();
        this.title = message.getTitle();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
        if (workspaceParticipant != null) {
            this.sender = new WorkspaceParticipantProfile(workspaceParticipant);
        } else {
            this.sender = null;
        }
    }
}
