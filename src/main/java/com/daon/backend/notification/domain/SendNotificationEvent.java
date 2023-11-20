package com.daon.backend.notification.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SendNotificationEvent {

    private NotificationType notificationType;

    private Object notificationData;

    private String memberId;

    public static SendNotificationEvent create(NotificationType notificationType, Object notificationData, String memberId) {
        return new SendNotificationEvent(notificationType, notificationData, memberId);
    }
}
