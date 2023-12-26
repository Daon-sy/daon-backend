package com.daon.backend.task.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WorkspaceSummaryForNotification {

    private Long workspaceId;

    private String workspaceTitle;
}
