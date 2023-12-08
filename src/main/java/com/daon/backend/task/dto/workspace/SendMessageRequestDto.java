package com.daon.backend.task.dto.workspace;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class SendMessageRequestDto {

    @NotBlank
    @Size(max = 50)
    private String title;

    @Size(max = 500)
    private String content;

    private Long workspaceParticipantId;
}
