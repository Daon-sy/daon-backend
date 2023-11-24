package com.daon.backend.task.domain.project;

import com.daon.backend.task.dto.notification.DeportationProjectAlarmResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeportationProjectAlarmEvent {

    private DeportationProjectAlarmResponseDto data;

    private String memberId;
}
