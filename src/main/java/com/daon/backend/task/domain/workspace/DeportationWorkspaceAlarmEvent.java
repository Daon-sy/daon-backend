package com.daon.backend.task.domain.workspace;

import com.daon.backend.task.dto.notification.DeportationWorkspaceAlarmResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeportationWorkspaceAlarmEvent {

    private DeportationWorkspaceAlarmResponseDto data;

    private String memberId;
}
