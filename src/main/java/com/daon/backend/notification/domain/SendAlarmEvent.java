package com.daon.backend.notification.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SendAlarmEvent {

    private NotificationType notificationType;

    private Object notificationData;

    private String memberId;

    public static SendAlarmEvent create(NotificationType notificationType, Object notificationData, String memberId) {
        return new SendAlarmEvent(notificationType, notificationData, memberId);
    }
}
