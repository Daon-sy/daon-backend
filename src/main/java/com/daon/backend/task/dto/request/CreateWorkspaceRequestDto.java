package com.daon.backend.task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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
        private String title;

        private String imageUrl;

        private String description;

        private String subject;

        @Builder
        public WorkspaceInfo(String title, String imageUrl, String description, String subject) {
            this.title = title;
            this.imageUrl = imageUrl;
            this.description = description;
            this.subject = subject;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class WorkspaceProfileInfo {

        @NotBlank
        private String name;

        private String imageUrl;

        private String email;

        @Builder
        public WorkspaceProfileInfo(String name, String imageUrl, String email) {
            this.name = name;
            this.imageUrl = imageUrl;
            this.email = email;
        }
    }

}
