package com.daon.backend.notification.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

    void save(Notification notification);

    Optional<Notification> findById(Long id);

    List<Notification> findNotificationsUnreadByMemberId(String memberId);

    Page<Notification> findNotificationsReadByMemberId(String memberId, Pageable pageable);

    List<Notification> findNotSentNotifications(String memberId, long now);

    void deleteById(Long id);
}
