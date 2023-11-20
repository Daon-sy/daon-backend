package com.daon.backend.task.dto;

import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.task.Reply;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ReplySummary {

    private Long replyId;

    private String content;

    private ReplyWriter writer;

    private boolean isWriter;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public ReplySummary(Reply reply, ProjectParticipant projectParticipant) {
        this.replyId = reply.getId();
        this.content = reply.getContent();
        this.writer = new ReplyWriter(projectParticipant);
        this.isWriter = reply.getWriter().getMemberId().equals(projectParticipant.getMemberId());
        this.createdAt = reply.getCreatedAt();
        this.modifiedAt = reply.getModifiedAt();
    }
}
