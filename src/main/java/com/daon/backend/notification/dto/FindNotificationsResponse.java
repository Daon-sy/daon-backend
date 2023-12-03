package com.daon.backend.notification.dto;

import com.daon.backend.notification.domain.Notification;
import com.daon.backend.notification.domain.NotificationType;
import lombok.Getter;

import java.util.List;

@Getter
public class FindNotificationsResponse {

    private List<NotificationSummary> notifications;

    public FindNotificationsResponse(List<NotificationSummary> notifications) {
        this.notifications = notifications;
    }

    @Getter
    public static class NotificationSummary {
        private String data;

        private NotificationType type;

        public NotificationSummary(Notification notification) {
            this.data = notification.getNotificationData();
            this.type = notification.getNotificationType();
        }
    }
}
