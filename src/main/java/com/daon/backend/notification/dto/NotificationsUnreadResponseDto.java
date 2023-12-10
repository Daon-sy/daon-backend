package com.daon.backend.notification.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class NotificationsUnreadResponseDto {

    private List<NotificationDto> notifications;

    public NotificationsUnreadResponseDto(List<NotificationDto> notifications) {
        this.notifications = notifications;
    }
}
