package com.daon.backend.task.dto;

import com.daon.backend.task.domain.board.Board;
import lombok.Getter;

@Getter
public class BoardSummary {

    private Long boardId;

    private String title;

    public BoardSummary(Board board) {
        this.boardId = board.getId();
        this.title = board.getTitle();
    }
}
