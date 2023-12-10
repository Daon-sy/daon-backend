package com.daon.backend.notification.dto;

import com.daon.backend.common.response.slice.PageResponse;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class NotificationsReadResponseDto extends PageResponse<NotificationDto> {

    public NotificationsReadResponseDto(Page<NotificationDto> page) {
        super(page);
    }
}
