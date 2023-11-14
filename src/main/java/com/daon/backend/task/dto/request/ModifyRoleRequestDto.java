package com.daon.backend.task.dto.request;

import com.daon.backend.task.domain.workspace.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyRoleRequestDto {

    private Long workspaceParticipantId;

    private Role role;
}
