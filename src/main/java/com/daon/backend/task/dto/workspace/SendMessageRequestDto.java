package com.daon.backend.task.dto.workspace;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendMessageRequestDto {

    private String title;

    private String content;

    private Long workspaceParticipantId;
}
