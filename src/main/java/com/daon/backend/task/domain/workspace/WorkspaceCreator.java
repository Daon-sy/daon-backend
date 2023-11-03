package com.daon.backend.task.domain.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WorkspaceCreator {

    private String memberId;
    private String profileName;
    private String profileImageUrl;
}
