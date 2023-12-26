package com.daon.backend.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskSearchParams {

    private Long projectId;

    private Long boardId;

    // 북마크 필터링 여부
    private boolean bookmarked = false;

    // 내 담당 할 일 필터링 여부
    private boolean my = false;
}
