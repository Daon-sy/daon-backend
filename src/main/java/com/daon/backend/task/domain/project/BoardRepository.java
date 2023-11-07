package com.daon.backend.task.domain.project;

public interface BoardRepository {

    Board save(Board board);

    boolean existsBoardByTitle(String title);
}
