package com.daon.backend.task.domain.workspace;

import com.daon.backend.task.dto.notification.InviteWorkspaceAlarmResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InviteWorkspaceAlarmEvent {

    private InviteWorkspaceAlarmResponseDto data;

    private String memberId;
}
