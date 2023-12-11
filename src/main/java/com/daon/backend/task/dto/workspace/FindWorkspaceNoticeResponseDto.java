package com.daon.backend.task.dto.workspace;

import com.daon.backend.task.domain.workspace.WorkspaceNotice;
import com.daon.backend.task.dto.WorkspaceNoticeSummary;
import com.daon.backend.task.dto.WorkspaceNoticeWriter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FindWorkspaceNoticeResponseDto {
    private Long noticeId;
    private WorkspaceNoticeWriter writer;
    private String title;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public FindWorkspaceNoticeResponseDto(WorkspaceNotice workspaceNotice){
        this.noticeId = workspaceNotice.getId();
        this.writer = new WorkspaceNoticeWriter(workspaceNotice.getWorkspaceNoticeWriter());
        this.title = workspaceNotice.getTitle();
        this.content = workspaceNotice.getContent();
        this.createdAt = workspaceNotice.getCreatedAt();
        this.modifiedAt = workspaceNotice.getModifiedAt();
    }
}
