package com.daon.backend.notification.infrastructure;

import com.daon.backend.notification.domain.Notification;
import com.daon.backend.notification.domain.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public void save(Notification notification) {
        notificationJpaRepository.save(notification);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationJpaRepository.findById(id);
    }

    @Override
    public List<Notification> findNotificationsUnreadByMemberId(String memberId) {
        return notificationJpaRepository.findByTargetMemberIdAndReadFalse(memberId);
    }

    @Override
    public Page<Notification> findNotificationsReadByMemberId(String memberId, Pageable pageable) {
        return notificationJpaRepository.findByTargetMemberIdAndReadTrue(memberId, pageable);
    }

    @Override
    public List<Notification> findNotSentNotifications(String memberId, long now) {
        return notificationJpaRepository.findByTargetMemberIdAndWhenEventPublishedGreaterThan(memberId, now);
    }

    @Override
    public void deleteById(Long id) {
        notificationJpaRepository.deleteById(id);
    }
}
