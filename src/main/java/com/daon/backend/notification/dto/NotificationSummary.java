package com.daon.backend.notification.dto;

import com.daon.backend.notification.domain.Notification;
import com.daon.backend.notification.domain.NotificationType;
import lombok.Getter;

@Getter
public class NotificationSummary {
    private Long notificationId;

    private String data;

    private NotificationType type;

    public NotificationSummary(Notification notification) {
        this.notificationId = notification.getId();
        this.data = notification.getNotificationData();
        this.type = notification.getNotificationType();
    }
}