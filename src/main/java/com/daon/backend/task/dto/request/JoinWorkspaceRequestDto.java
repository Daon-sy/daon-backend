package com.daon.backend.task.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class JoinWorkspaceRequestDto {

    private String joinCode;

    private WorkspaceProfileInfo profile;

    @Getter
    @NoArgsConstructor
    public static class WorkspaceProfileInfo {

        @NotBlank
        private String name;

        private String imageUrl;

        @Email
        private String email;
    }
}
