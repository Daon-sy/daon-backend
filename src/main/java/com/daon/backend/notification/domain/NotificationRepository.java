package com.daon.backend.notification.domain;

import java.util.List;

public interface NotificationRepository {

    Notification save(Notification notification);

    List<Notification> findNotSentNotifications(String memberId, long now);
}
