package com.daon.backend.notification.infrastructure;

import com.daon.backend.member.service.SessionMemberProvider;
import com.daon.backend.notification.domain.*;
import com.daon.backend.notification.dto.TasksNotificationParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final SessionMemberProvider sessionMemberProvider;
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    private static final String TASKS_EMITTER_ID_PREFIX = "tasks_";


    /**
     * 알람 이벤트 구독
     */
    public SseEmitter subscribeAlarm(String lastEventId) {
        String memberId = sessionMemberProvider.getMemberId();
        String emitterId = memberId + "_" + System.currentTimeMillis();

        SseEmitter emitter = emitterInitialSetting(emitterId, memberId);

        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }

        return emitter;
    }

    private SseEmitter emitterInitialSetting(String emitterId, String memberId) {
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(Long.MAX_VALUE));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        emitter.onError((e) -> emitterRepository.deleteById(emitterId));

        // 503 에러 방지
        String errorPreventionData = "EventStream Created. [userId = " + memberId + "]";
        sendNotification(emitter, emitterId, emitterId, errorPreventionData);
        return emitter;
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, String memberId, String emitterId, SseEmitter emitter) {
        Map<String, SseEmitter> eventCaches = emitterRepository.findAllEmitterStartWithMemberId(memberId);
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    /**
     * 알림 이벤트 응답 보내기
     */
    public void sendAlarm(NotificationType type, Object data, String memberId) throws JsonProcessingException {
        String notificationData = objectMapper.writeValueAsString(data);

        Notification notification = notificationRepository.save(createNotification(type, notificationData, memberId));
        String eventId = memberId + "_" + System.currentTimeMillis();

        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithMemberId(memberId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, notificationData);
                }
        );
    }

    private Notification createNotification(NotificationType notificationType, String notificationData, String memberId) {
        return Notification.builder()
                .notificationType(notificationType)
                .notificationData(notificationData)
                .memberId(memberId)
                .build();
    }

    /**
     * 할 일 목록 이벤트 구독
     */
    public SseEmitter subscribeFindTasks(Long workspaceId, TasksNotificationParams params) {
        if (!params.checkValidRequest()) {
            throw new TypeNotSpecifiedException();
        }

        String memberId = sessionMemberProvider.getMemberId();
        String emitterId = TASKS_EMITTER_ID_PREFIX + workspaceId + params.getSuffixByParam();

        return emitterInitialSetting(emitterId, memberId);
    }

    /**
     * 할 일 목록 이벤트 응답 보내기
     */
    public void sendFindTasksEventResponse(Long workspaceId, Long projectId, Long boardId) {
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmittersStartWithTasks();
        String DEFAULT_MESSAGE = "할 일이 변경되었습니다. 조회 요청을 보내주세요.";

        emitters.forEach((key, emitter) -> {
            if (key.startsWith(TASKS_EMITTER_ID_PREFIX)) {
                String[] conditions = key.split("_");
                String paramType = conditions[2];
                Long emitterWorkspaceId = Long.parseLong(conditions[1]);

                switch (paramType) {
                    case "board":
                        if (workspaceId.equals(emitterWorkspaceId) &&
                                projectId.equals(Long.parseLong(conditions[5])) &&
                                boardId.equals(Long.parseLong(conditions[3]))) {
                            sendNotification(emitter, key, key, DEFAULT_MESSAGE);
                        }
                        break;
                    case "project":
                        if (workspaceId.equals(emitterWorkspaceId) && projectId.equals(Long.parseLong(conditions[3]))) {
                            sendNotification(emitter, key, key, DEFAULT_MESSAGE);
                        }
                        break;
                    case "my":
                    case "bookmark":
                        if (workspaceId.equals(emitterWorkspaceId)) {
                            sendNotification(emitter, key, key, DEFAULT_MESSAGE);
                        }
                        break;
                }
            }
        });
    }
}
