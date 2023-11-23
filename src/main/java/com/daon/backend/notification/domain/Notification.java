package com.daon.backend.notification.domain;

import com.daon.backend.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    private String notificationData;

    private String memberId;

    @Builder
    public Notification(NotificationType notificationType, String notificationData, String memberId) {
        this.notificationType = notificationType;
        this.notificationData = notificationData;
        this.memberId = memberId;
    }
}
