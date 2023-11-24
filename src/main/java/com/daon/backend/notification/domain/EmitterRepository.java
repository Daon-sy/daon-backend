package com.daon.backend.notification.domain;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {

    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    Map<String, SseEmitter> findAll();

    Map<String, SseEmitter> findAllEmitterStartWithMemberId(String memberId);

    Map<String, SseEmitter> findAllEmittersStartWithTasks();

    Map<String, SseEmitter> findAllEmitterStartWithTask();

    void deleteById(String id);
}
