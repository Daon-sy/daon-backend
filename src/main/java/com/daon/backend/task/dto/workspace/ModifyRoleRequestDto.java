package com.daon.backend.task.dto.workspace;

import com.daon.backend.task.domain.workspace.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyRoleRequestDto {

    @NotNull
    private Long workspaceParticipantId;

    @NotBlank
    private Role role;
}
