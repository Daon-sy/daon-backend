package com.daon.backend.task.domain.project;

import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectCreator {

    private String memberId;
    private WorkspaceParticipant workspaceParticipant;
}
