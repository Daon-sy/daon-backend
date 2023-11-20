package com.daon.backend.notification.infrastructure;

import com.daon.backend.member.service.SessionMemberProvider;
import com.daon.backend.notification.domain.EmitterRepository;
import com.daon.backend.notification.domain.Notification;
import com.daon.backend.notification.domain.NotificationRepository;
import com.daon.backend.notification.domain.NotificationType;
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

    /**
     * SSE 이벤트 구독
     */
    public SseEmitter subscribeAlarm(String lastEventId) {
        String memberId = sessionMemberProvider.getMemberId();
        String emitterId = memberId + "_" + System.currentTimeMillis();

        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(Long.MAX_VALUE));
        log.info("emitter 저장 : {}", emitterId);
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        emitter.onError((e) -> emitterRepository.deleteById(emitterId));

        // 503 에러 방지
        String errorPreventionData = "EventStream Created. [userId = " + memberId + "]";
        sendNotification(emitter, emitterId, emitterId, errorPreventionData);

        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }

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
        Map<String, SseEmitter> eventCaches = emitterRepository.findAllEmitterStartWithByMemberId(memberId);
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    /**
     * 알림 보내기
     */
    public void send(NotificationType type, Object data, String memberId) throws JsonProcessingException {
        String notificationData = objectMapper.writeValueAsString(data);

        Notification notification = notificationRepository.save(createNotification(type, notificationData, memberId));
        String eventId = memberId + "_" + System.currentTimeMillis();

        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(memberId);
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
}
