package com.daon.backend.notification.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SendFindTasksEvent {

    private Long workspaceId;

    private Long projectId;

    private Long boardId;

    public static SendFindTasksEvent create(Long workspaceId, Long projectId, Long boardId) {
        return new SendFindTasksEvent(workspaceId, projectId, boardId);
    }
}
