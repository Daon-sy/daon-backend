package com.daon.backend.task.domain.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Subject {
    IT_DEV_TEAM("IT개발팀"),
    CLUB("동아리/동호회"),
    ;

    private final String description;

}
