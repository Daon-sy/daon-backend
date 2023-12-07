package com.daon.backend.task.dto.workspace;

import com.daon.backend.task.dto.WorkspaceNoticeSummary;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FindWorkspaceNoticesResponseDto {

    private int totalCount;
    private List<WorkspaceNoticeSummary> workspaceNotices;

    public FindWorkspaceNoticesResponseDto(List<WorkspaceNoticeSummary> workspaceNotices){
        this.totalCount = workspaceNotices.size();
        this.workspaceNotices = workspaceNotices;
    }

}
