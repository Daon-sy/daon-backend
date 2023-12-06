package com.daon.backend.task.dto.workspace;

import com.daon.backend.task.domain.workspace.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class InviteMemberRequestDto {

    @NotBlank
    private String username;

    private Role role;
}
