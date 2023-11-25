package com.daon.backend.task.domain.task;

import com.daon.backend.task.dto.notification.DesignatedManagerAlarmResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DesignatedManagerAlarmEvent {

    private DesignatedManagerAlarmResponseDto data;

    private String memberId;
}
