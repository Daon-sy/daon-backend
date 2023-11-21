package com.daon.backend.notification.domain;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {

    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    void saveEventCache(String eventCacheId, Object event);

    Map<String, SseEmitter> findAllEmitterStartWithMemberId(String memberId);

    Map<String, SseEmitter> findAllEmittersStartWithTasks();

    void deleteById(String id);

    void deleteAllEmitterStartWith(String memberId);
}
