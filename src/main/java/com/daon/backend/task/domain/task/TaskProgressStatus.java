package com.daon.backend.task.domain.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskProgressStatus {

    TODO("해야할 일"),
    PROCEEDING("진행중"),
    COMPLETED("완료됨"),
    PENDING("보류중"),
    ;

    private final String description;

}
