package com.daon.backend.task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinWorkspaceRequestDto {

    private String joinCode;

    private WorkspaceProfileInfo profile;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkspaceProfileInfo {

        @NotBlank
        private String name;

        private String imageUrl;

        @Email
        private String email;
    }
}
