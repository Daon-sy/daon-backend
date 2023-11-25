package com.daon.backend.task.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectSummaryForNotification {

    private Long projectId;

    private String projectTitle;
}

