package com.daon.backend.task.dto.task.history;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskHistory {

    private String fieldName;
    private String fieldType;
    private Object from;
    private Object to;
    private HistoryProjectParticipant modifier;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public TaskHistory(String fieldName, String fieldType, Object from, Object to,
                       HistoryProjectParticipant modifier, LocalDateTime modifiedAt) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.from = from;
        this.to = to;
        this.modifier = modifier;
        this.modifiedAt = modifiedAt;
    }
}
