package com.daon.backend.notification.dto.response;

import com.daon.backend.notification.dto.WorkspaceSummary;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InviteProjectAlarmResponseDto {

    private WorkspaceSummary workspaceSummary;
}
