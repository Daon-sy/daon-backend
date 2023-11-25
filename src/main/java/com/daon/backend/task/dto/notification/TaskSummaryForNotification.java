package com.daon.backend.task.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskSummaryForNotification {

    private Long taskId;

    private String taskTitle;
}

