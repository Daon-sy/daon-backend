package com.daon.backend.task.dto;

import com.daon.backend.task.domain.workspace.WorkspaceNotice;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkspaceNoticeSummary {

    private Long noticeId;
    private WorkspaceNoticeWriter writer;
    private String title;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public  WorkspaceNoticeSummary(Long noticeId, WorkspaceParticipant workspaceParticipant,
                                   String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt){
        this.noticeId = noticeId;
        this.writer = new WorkspaceNoticeWriter(workspaceParticipant);
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public WorkspaceNoticeSummary(WorkspaceNotice workspaceNotice){
        this.noticeId = workspaceNotice.getId();
        this.writer = new WorkspaceNoticeWriter(workspaceNotice.getWorkspaceNoticeWriter());
        this.title = workspaceNotice.getTitle();
        this.content = workspaceNotice.getContent();
        this.createdAt = workspaceNotice.getCreatedAt();
        this.modifiedAt = workspaceNotice.getModifiedAt();
    }

}
