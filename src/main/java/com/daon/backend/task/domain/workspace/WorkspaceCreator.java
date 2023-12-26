package com.daon.backend.task.domain.workspace;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WorkspaceCreator {

    private String memberId;
    private String profileName;
    private String profileImageUrl;
    private String profileEmail;

    @Builder
    public WorkspaceCreator(String memberId, String profileName, String profileImageUrl, String profileEmail) {
        this.memberId = memberId;
        this.profileName = profileName;
        this.profileImageUrl = profileImageUrl;
        this.profileEmail = profileEmail;
    }
}
