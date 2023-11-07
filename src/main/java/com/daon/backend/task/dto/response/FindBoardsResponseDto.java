package com.daon.backend.task.dto.response;

import com.daon.backend.task.domain.project.Board;
import lombok.Getter;

import java.util.List;

@Getter
public class FindBoardsResponseDto {

    private int totalCount;

    private List<BoardInfo> boards;

    public FindBoardsResponseDto(List<BoardInfo> boards) {
        this.totalCount = boards.size();
        this.boards = boards;
    }

    @Getter
    public static class BoardInfo {
        private Long boardId;

        private String title;

        public BoardInfo(Board board) {
            this.boardId = board.getId();
            this.title = board.getTitle();
        }
    }
}
