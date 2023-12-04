package com.daon.backend.notification.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class FindNotificationsResponse {

    private List<NotificationSummary> notifications;

    public FindNotificationsResponse(List<NotificationSummary> notifications) {
        this.notifications = notifications;
    }
}
