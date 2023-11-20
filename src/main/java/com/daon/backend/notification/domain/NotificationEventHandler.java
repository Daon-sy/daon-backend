package com.daon.backend.notification.domain;

import com.daon.backend.notification.infrastructure.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class NotificationEventHandler {

    private final NotificationService notificationService;

    @TransactionalEventListener
    @Async
    public void handle(SendNotificationEvent event) throws JsonProcessingException {
        notificationService.send(event.getNotificationType(), event.getNotificationData(), event.getMemberId());
    }
}
