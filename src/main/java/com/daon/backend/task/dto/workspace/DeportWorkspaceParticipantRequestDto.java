package com.daon.backend.task.dto.workspace;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class DeportWorkspaceParticipantRequestDto {

    @NotNull
    private Long workspaceParticipantId;
}
