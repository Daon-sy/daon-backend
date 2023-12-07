package com.daon.backend.task.domain.workspace;

import com.daon.backend.task.dto.notification.SendReceiveMessageAlarmResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendReceiveMessageAlarmEvent {

    private SendReceiveMessageAlarmResponseDto data;

    private String memberId;
}
