package com.daon.backend.task.domain.project;

import com.daon.backend.task.dto.notification.InviteProjectAlarmResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InviteProjectAlarmEvent {

    private InviteProjectAlarmResponseDto data;

    private String memberId;
}
