package com.daon.backend.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskSummary {

    private Long taskId;

    private String taskTitle;
}
