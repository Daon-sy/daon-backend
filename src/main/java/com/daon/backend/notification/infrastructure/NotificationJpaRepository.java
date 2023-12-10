package com.daon.backend.notification.infrastructure;

import com.daon.backend.notification.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationJpaRepository<T extends Notification> extends JpaRepository<T, Long> {

    List<Notification> findByTargetMemberIdAndReadFalseOrderByCreatedAtDesc(String memberId);

    Page<Notification> findByTargetMemberIdAndReadTrueOrderByCreatedAtDesc(String memberId, Pageable pageable);

    List<Notification> findByTargetMemberIdAndWhenEventPublishedGreaterThanOrderByCreatedAtDesc(String memberId, long now);
}
