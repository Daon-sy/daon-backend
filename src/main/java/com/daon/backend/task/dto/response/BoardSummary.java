package com.daon.backend.task.dto.response;

import com.daon.backend.task.domain.project.Board;
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
