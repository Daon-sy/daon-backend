package com.daon.backend.task.dto.task.history;

import lombok.Getter;

@Getter
public class HistoryBoard {

    private Long boardId;
    private String title;

    public HistoryBoard(Long boardId, String title) {
        this.boardId = boardId;
        this.title = title;
    }
}
