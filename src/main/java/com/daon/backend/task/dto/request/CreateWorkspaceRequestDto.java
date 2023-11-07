package com.daon.backend.task.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CreateWorkspaceRequestDto {

    @Valid
    private WorkspaceInfo workspace;

    @Valid
    private WorkspaceProfileInfo profile;

    @Getter
    @NoArgsConstructor
    public static class WorkspaceInfo {

        @NotBlank
        private String title;

        private String imageUrl;

        private String description;

        private String subject;
    }

    @Getter
    @NoArgsConstructor
    public static class WorkspaceProfileInfo {

        @NotBlank
        private String name;

        private String imageUrl;

        private String email;
    }

}
