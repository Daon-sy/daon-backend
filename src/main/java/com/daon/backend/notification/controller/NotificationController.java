package com.daon.backend.notification.controller;

import com.daon.backend.notification.infrastructure.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "실시간 이벤트(알림) 구독", description = "실시간 이벤트(알림) 구독 요청입니다.")
    @GetMapping(value = "/api/subscribe/alarm", produces = "text/event-stream")
    public SseEmitter subscribeAlarm(
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

        return notificationService.subscribeAlarm(lastEventId);
    }
}
