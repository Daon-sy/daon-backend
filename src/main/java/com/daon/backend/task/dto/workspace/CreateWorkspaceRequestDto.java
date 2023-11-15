package com.daon.backend.task.dto.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkspaceRequestDto {

    @Valid
    private WorkspaceInfo workspace;

    @Valid
    private WorkspaceProfileInfo profile;

    @Getter
    @NoArgsConstructor
    public static class WorkspaceInfo {

        @NotBlank
        @Size(max = 20)
        private String title;

        private String imageUrl;

        @Size(max = 100)
        private String description;

        @Size(max = 10)
        private String subject;
    }

    @Getter
    @NoArgsConstructor
    public static class WorkspaceProfileInfo {

        @NotBlank
        @Size(max = 20)
        private String name;

        private String imageUrl;

        private String email;
    }

}
