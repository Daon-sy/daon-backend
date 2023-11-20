package com.daon.backend.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    INVITE_WORKSPACE("워크스페이스에 초대 받음"),
    INVITE_PROJECT("프로젝트에 초대 받음"),
    DEPORTATION_WORKSPACE("워크스페이스에서 추방됨"),
    DEPORTATION_PROJECT("프로젝트에서 추방됨"),
    REGISTERED_TASK_MANAGER("할 일 담당자로 지정됨"),
    ;

    private final String descriptionType;
}
