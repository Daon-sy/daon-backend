package com.daon.backend.task.dto;

import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.task.TaskReply;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskReplySummary {

    private Long replyId;

    private String content;

    private TaskReplyWriter taskReplyWriter;

    private Boolean isWriter;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public TaskReplySummary(TaskReply taskReply, ProjectParticipant currentParticipant) {
        this.replyId = taskReply.getId();
        this.content = taskReply.getContent();
        this.taskReplyWriter = new TaskReplyWriter(taskReply.getTaskReplyWriter());
        this.isWriter = taskReply.getTaskReplyWriter().getMemberId().equals(currentParticipant.getMemberId());
        this.createdAt = taskReply.getCreatedAt();
        this.modifiedAt = taskReply.getModifiedAt();
    }
}
