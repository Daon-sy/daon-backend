package com.daon.backend.notification.infrastructure;

import com.daon.backend.notification.domain.Notification;
import com.daon.backend.notification.domain.NotificationRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.daon.backend.notification.domain.QNotification.notification;

@RequiredArgsConstructor
@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Notification save(Notification notification) {
        return notificationJpaRepository.save(notification);
    }

    @Override
    public List<Notification> findNotSentNotifications(String memberId, long now) {
        return queryFactory
                .selectFrom(notification)
                .where(notification.whenEventPublished.gt(now)
                        .and(notification.memberId.eq(memberId)))
                .fetch();
    }
}
