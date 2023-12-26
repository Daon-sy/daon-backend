package com.daon.backend.task.dto.notification;

import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.daon.backend.task.dto.WorkspaceParticipantProfile;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SendReceiveMessageAlarmResponseDto {

    private WorkspaceSummaryForNotification workspace;

    private WorkspaceParticipantProfile sender;

    private Long messageId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    public SendReceiveMessageAlarmResponseDto(Long workspaceId,
                                              String workspaceTitle,
                                              WorkspaceParticipant sender,
                                              Long messageId,
                                              LocalDateTime createdAt) {
        this.workspace = new WorkspaceSummaryForNotification(workspaceId, workspaceTitle);
        this.sender = new WorkspaceParticipantProfile(sender);
        this.messageId = messageId;
        this.time = createdAt;
    }
}
