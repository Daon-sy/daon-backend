package com.daon.backend.notification.controller;

import com.daon.backend.notification.dto.NotificationsReadResponseDto;
import com.daon.backend.notification.dto.NotificationsUnreadResponseDto;
import com.daon.backend.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "미확인 알림 목록 조회", description = "미확인 알림 목록 조회 요청입니다.")
    @GetMapping("/api/notifications-unread")
    public NotificationsUnreadResponseDto findNotificationsUnread() {
        return notificationService.findNotificationsUnread();
    }

    @Operation(summary = "확인한 알림 목록 조회", description = "확인한 알림 목록 조회 요청입니다.")
    @GetMapping("/api/notifications-read")
    public NotificationsReadResponseDto findNotificationsRead(Pageable pageable) {
        return notificationService.findNotificationsRead(pageable);
    }

    @Operation(summary = "알림 읽음 처리", description = "알림 읽음 처리 요청입니다.")
    @PostMapping("/api/notifications/{notificationId}/read")
    public void readNotification(@PathVariable Long notificationId) {
        notificationService.readNotification(notificationId);
    }

    @Operation(summary = "알림 삭제 처리", description = "알림 삭제 처리 요청입니다.")
    @DeleteMapping("/api/notifications/{notificationId}")
    public void deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }
}
