package com.daon.backend.task.dto;

import com.daon.backend.task.domain.workspace.WorkspaceNotice;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkspaceNoticeSummary {

    private Long workspaceNoticeId;
    private Long workspaceId;
    private WorkspaceNoticeWriter writer;
    private String title;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public  WorkspaceNoticeSummary(Long workspaceNoticeId, WorkspaceSummary workspace, WorkspaceParticipant workspaceParticipant,
                                   String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt){
        this.workspaceNoticeId = workspaceNoticeId;
        this.workspaceId = workspace.getWorkspaceId();
        this.writer = new WorkspaceNoticeWriter(workspaceParticipant);
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;

    }
    public WorkspaceNoticeSummary(WorkspaceNotice workspaceNotice){
        this.workspaceNoticeId = workspaceNotice.getId();
        this.workspaceId = workspaceNotice.getWorkspace().getId();
        this.writer = new WorkspaceNoticeWriter(workspaceNotice.getWorkspaceNoticeWriter());
        this.title = workspaceNotice.getTitle();
        this.content = workspaceNotice.getContent();
        this.createdAt = workspaceNotice.getCreatedAt();
        this.modifiedAt = workspaceNotice.getModifiedAt();
    }

}
