package com.daon.backend.notification.service;

import com.daon.backend.notification.dto.NotificationDto;
import com.daon.backend.notification.dto.NotificationsReadResponseDto;
import com.daon.backend.notification.dto.NotificationsUnreadResponseDto;
import com.daon.backend.notification.domain.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final SessionMemberProvider sessionMemberProvider;
    private final NotificationRepository notificationRepository;

    public NotificationsUnreadResponseDto findNotificationsUnread() {
        return new NotificationsUnreadResponseDto(
                notificationRepository.findNotificationsUnreadByMemberId(sessionMemberProvider.getMemberId()).stream()
                        .map(NotificationDto::new)
                        .collect(Collectors.toList())
        );
    }

    public NotificationsReadResponseDto findNotificationsRead(Pageable pageable) {
        return new NotificationsReadResponseDto(
                notificationRepository.findNotificationsReadByMemberId(
                        sessionMemberProvider.getMemberId(),
                        pageable
                ).map(NotificationDto::new)
        );
    }

    @Transactional
    public void readNotification(Long notificationId) {
        notificationRepository.findById(notificationId).orElseThrow().doRead();
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

//    /**
//     * 알림 읽음 처리
//     */
//    public void readNotification(Long notificationId) {
//        notificationRepositoryInfra.readNotification(notificationId);
//    }
//
//    /**
//     * 알림 목록 삭제
//     */
//    public void deleteNotifications() {
//        String memberId = sessionMemberProvider.getMemberId();
//        notificationRepositoryInfra.deleteNotifications(memberId);
//    }

}
