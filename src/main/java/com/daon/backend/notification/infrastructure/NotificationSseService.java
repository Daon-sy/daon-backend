package com.daon.backend.notification.infrastructure;

import com.daon.backend.notification.domain.*;
import com.daon.backend.notification.dto.NotificationDto;
import com.daon.backend.notification.dto.TasksNotificationParams;
import com.daon.backend.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class NotificationSseService {

    private final SessionMemberProviderImpl sessionMemberProvider;
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    private static final String TASKS_EMITTER_ID_PREFIX = "tasks_";
    private static final String TASK_EMITTER_ID_PREFIX = "task_";
    private static final String NOTIFICATION_TYPE_MESSAGE = "MESSAGE";


    /**
     * 알람 이벤트 구독
     */
    public SseEmitter subscribeAlarm(String lastEventId) {
        String memberId = sessionMemberProvider.getMemberId();
        String emitterId = memberId + "_" + System.currentTimeMillis();

        SseEmitter emitter = emitterInitialSetting(emitterId);

        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }

        return emitter;
    }

    private SseEmitter emitterInitialSetting(String emitterId) {
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(Long.MAX_VALUE));
        emitter.onCompletion(() -> {
            emitterRepository.deleteById(emitterId);
            log.debug("emitter completed... emitter-id: {}", emitterId);
        });
        emitter.onTimeout(() -> {
            emitterRepository.deleteById(emitterId);
            log.debug("emitter timeout... emitter-id: {}", emitterId);
        });
        emitter.onError((e) -> emitter.complete());

        // 503 에러 방지
        String connectType = "CONNECTED";
        sendNotification(emitter, emitterId, emitterId, null, connectType);
        return emitter;
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data, String type) {
        try {
            SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event()
                    .id(eventId)
                    .name(type);
            emitter.send(eventBuilder.data(Objects.requireNonNullElse(data, "")));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, String memberId, String emitterId, SseEmitter emitter) {
        long now = Long.parseLong(lastEventId.substring(memberId.length() + 1));

        notificationRepository.findNotSentNotifications(memberId, now)
                .forEach(notification -> sendNotification(
                        emitter,
                        emitterId,
                        emitterId,
                        new NotificationDto(notification).getData(),
                        String.valueOf(notification.getNotificationType())
                ));
    }

    /**
     * 알림 이벤트 응답 보내기
     */
    public void sendAlarm(Notification notification) {
        long whenEventPublished = System.currentTimeMillis();

        String memberId = notification.getTargetMemberId();
        String eventId = memberId + "_" + whenEventPublished;

        notificationRepository.save(notification);
        NotificationDto notificationDto = new NotificationDto(notification);

        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithMemberId(memberId);
        emitters.forEach(
                (key, emitter) -> sendNotification(
                        emitter,
                        eventId,
                        key,
                        notificationDto,
                        String.valueOf(notification.getNotificationType())
                )
        );
    }

    /**
     * 할 일 목록 이벤트 구독
     */
    public SseEmitter subscribeTasks(Long workspaceId, TasksNotificationParams params) {
        if (!params.checkValidRequest()) {
            throw new TypeNotSpecifiedException();
        }

        String memberId = sessionMemberProvider.getMemberId();
        String emitterId = TASKS_EMITTER_ID_PREFIX + workspaceId + params.getSuffixByParam() + memberId;

        return emitterInitialSetting(emitterId);
    }

    /**
     * 할 일 목록 이벤트 응답 보내기
     */
    public void sendFindTasksEventNotification(Long workspaceId, Long projectId, Long boardId) {
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmittersStartWithTasks();
        emitters.forEach((key, emitter) -> {
            String[] conditions = key.split("_");
            String paramType = conditions[2];
            Long emitterWorkspaceId = Long.parseLong(conditions[1]);

            switch (paramType) {
                case "board":
                    if (workspaceId.equals(emitterWorkspaceId) &&
                            projectId.equals(Long.parseLong(conditions[5])) &&
                            boardId.equals(Long.parseLong(conditions[3]))) {
                        sendNotification(emitter, key, key, null, NOTIFICATION_TYPE_MESSAGE);
                    }
                    break;
                case "project":
                    if (workspaceId.equals(emitterWorkspaceId) && projectId.equals(Long.parseLong(conditions[3]))) {
                        sendNotification(emitter, key, key, null, NOTIFICATION_TYPE_MESSAGE);
                    }
                    break;
                case "my":
                case "bookmark":
                    if (workspaceId.equals(emitterWorkspaceId)) {
                        sendNotification(emitter, key, key, null, NOTIFICATION_TYPE_MESSAGE);
                    }
                    break;
            }
        });
    }

    /**
     * 할 일 상세 보기 이벤트 구독
     */
    public SseEmitter subscribeTask(Long taskId) {
        String memberId = sessionMemberProvider.getMemberId();
        String emitterId = TASK_EMITTER_ID_PREFIX + taskId + "_" + memberId;

        return emitterInitialSetting(emitterId);
    }

    /**
     * 할 일 상세 보기 이벤트 응답
     */
    public void sendFindTaskEventNotification(Long taskId) {
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithTask();
        emitters.forEach((key, emitter) -> {
            Long findTaskId = Long.valueOf(StringUtils.substringBetween(key, TASK_EMITTER_ID_PREFIX, "_"));
            if (Objects.equals(taskId, findTaskId)) {
                sendNotification(emitter, key, key, null, NOTIFICATION_TYPE_MESSAGE);
            }
        });
    }

    @Scheduled(fixedRate = 130_000)
    protected void sendHeartbeat() {
        Map<String, SseEmitter> emitterMap = emitterRepository.findAll();
        log.debug("send heartbeat to all emitter... emitter count: {}", emitterMap.values().size());

        emitterMap.forEach((key, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("heartbeat").data(""));
            } catch (Exception e) {
                emitterRepository.deleteById(key);
            }
        });
    }

}
