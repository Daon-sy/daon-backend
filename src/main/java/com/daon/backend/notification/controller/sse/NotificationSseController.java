package com.daon.backend.notification.controller.sse;

import com.daon.backend.notification.dto.TasksNotificationParams;
import com.daon.backend.notification.infrastructure.NotificationSseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class NotificationSseController {

    private final NotificationSseService notificationSseService;

    @Operation(summary = "실시간 이벤트(알림) 구독", description = "실시간 이벤트(알림) 구독 요청입니다.")
    @GetMapping(value = "/notifications/subscribe", produces = "text/event-stream")
    public SseEmitter subscribeAlarm(
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

        return notificationSseService.subscribeAlarm(lastEventId);
    }

    @Operation(summary = "실시간 이벤트(할 일 목록 조회) 구독", description = "실시간 이벤트(할 일 목록 조회) 구독 요청입니다.")
    @GetMapping(value = "/subscribe/workspaces/{workspaceId}/type", produces = "text/event-stream")
    public SseEmitter subscribeTasks(@PathVariable Long workspaceId ,
                                     @ModelAttribute TasksNotificationParams params) {

        return notificationSseService.subscribeTasks(workspaceId, params);
    }

    @Operation(summary = "실시간 이벤트(할 일 상세 조회) 구독", description = "실시간 이벤트(할 일 상세 조회) 구독 요청입니다.")
    @GetMapping( value = "/subscribe/workspaces/projects/tasks/{taskId}", produces = "text/event-stream")
    public SseEmitter subscribeTask(@PathVariable Long taskId) {

        return notificationSseService.subscribeTask(taskId);
    }
}
