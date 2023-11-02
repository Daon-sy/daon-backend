package com.daon.backend.task.domain.project;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskProgressStatus {

    PENDING("대기중"),
    PROCEEDING("진행중"),
    COMPLETED("완료됨")
    ;

    private final String description;

}
