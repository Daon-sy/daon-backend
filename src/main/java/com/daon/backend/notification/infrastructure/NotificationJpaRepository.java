package com.daon.backend.notification.infrastructure;

import com.daon.backend.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {

    List<Notification> findNotificationsByMemberId(String memberId);

    void deleteAllByMemberId(String memberId);
}
