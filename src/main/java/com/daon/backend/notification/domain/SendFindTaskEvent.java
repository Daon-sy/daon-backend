package com.daon.backend.notification.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SendFindTaskEvent {

    private Long taskId;

    public static SendFindTaskEvent create(Long taskId) {
        return new SendFindTaskEvent(taskId);
    }
}
