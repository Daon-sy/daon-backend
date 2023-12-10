package com.daon.backend.notification.controller;

import com.daon.backend.notification.dto.FindNotificationsResponse;
import com.daon.backend.notification.dto.TasksNotificationParams;
import com.daon.backend.notification.infrastructure.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "실시간 이벤트(알림) 구독", description = "실시간 이벤트(알림) 구독 요청입니다.")
    @GetMapping(value = "/notifications/subscribe", produces = "text/event-stream")
    public SseEmitter subscribeAlarm(
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

        return notificationService.subscribeAlarm(lastEventId);
    }

    @Operation(summary = "실시간 이벤트(할 일 목록 조회) 구독", description = "실시간 이벤트(할 일 목록 조회) 구독 요청입니다.")
    @GetMapping(value = "/subscribe/workspaces/{workspaceId}/type", produces = "text/event-stream")
    public SseEmitter subscribeTasks(@PathVariable Long workspaceId ,
                                     @ModelAttribute TasksNotificationParams params) {

        return notificationService.subscribeTasks(workspaceId, params);
    }

    @Operation(summary = "실시간 이벤트(할 일 상세 조회) 구독", description = "실시간 이벤트(할 일 상세 조회) 구독 요청입니다.")
    @GetMapping(value = "/subscribe/workspaces/projects/tasks/{taskId}", produces = "text/event-stream")
    public SseEmitter subscribeTask(@PathVariable Long taskId) {

        return notificationService.subscribeTask(taskId);
    }

    @Operation(summary = "알림 목록 조회", description = "알림 목록 조회 요청입니다.")
    @GetMapping("/notifications")
    public FindNotificationsResponse findNotifications() {

        return notificationService.findNotifications();
    }

    @Operation(summary = "알림 읽음 처리", description = "알림 읽음 처리 요청입니다.")
    @DeleteMapping("/notifications/{notificationId}")
    public void readNotification(@PathVariable Long notificationId) {
        notificationService.readNotification(notificationId);
    }

    @Operation(summary = "알림 목록 삭제", description = "알림 목록 삭제 요청입니다.")
    @DeleteMapping("/notifications")
    public void deleteNotifications() {
        notificationService.deleteNotifications();
    }
}
