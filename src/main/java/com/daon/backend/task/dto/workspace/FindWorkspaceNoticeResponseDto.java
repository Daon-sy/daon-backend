package com.daon.backend.task.dto.workspace;

import com.daon.backend.task.domain.workspace.WorkspaceNotice;
import com.daon.backend.task.dto.WorkspaceNoticeSummary;
import lombok.Getter;

@Getter
public class FindWorkspaceNoticeResponseDto {
    private WorkspaceNoticeSummary workspaceNotice;

    public FindWorkspaceNoticeResponseDto(WorkspaceNotice workspaceNotice){
        this.workspaceNotice = new WorkspaceNoticeSummary(workspaceNotice);
    }
}
