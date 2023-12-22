package com.daon.backend.notification.infrastructure;

import com.daon.backend.notification.domain.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseConnectionChecker {

    private final EmitterRepository emitterRepository;

    @Scheduled(fixedRate = 120_000)
    protected void sendHeartbeat() {
        Map<String, SseEmitter> emitterMap = emitterRepository.findAll();
        log.debug("send heartbeat to all emitter... emitter count: {}", emitterMap.values().size());

        emitterMap.forEach((key, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("heartbeat").data(""));
            } catch (Exception e) {
                log.debug("{}", e.getMessage());
                emitterRepository.deleteById(key);
            }
        });
    }
}
