package com.daon.backend.task.dto.workspace;

import com.daon.backend.task.domain.workspace.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckRoleResponseDto {

    private Role role;
}
