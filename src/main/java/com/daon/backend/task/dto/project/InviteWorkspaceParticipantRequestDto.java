package com.daon.backend.task.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InviteWorkspaceParticipantRequestDto {

    @NotNull
    private Long workspaceParticipantId;
}
