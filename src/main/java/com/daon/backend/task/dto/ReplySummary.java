package com.daon.backend.task.dto;

import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.task.Reply;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReplySummary {

    private Long replyId;

    private String content;

    private ReplyWriter writer;

    private Boolean isWriter;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public ReplySummary(Reply reply, ProjectParticipant currentParticipant) {
        this.replyId = reply.getId();
        this.content = reply.getContent();
        this.writer = new ReplyWriter(reply.getWriter());
        this.isWriter = reply.getWriter().getMemberId().equals(currentParticipant.getMemberId());
        this.createdAt = reply.getCreatedAt();
        this.modifiedAt = reply.getModifiedAt();
    }
}
