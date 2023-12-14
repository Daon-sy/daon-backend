package com.daon.backend.task.infrastructure.board;

import com.daon.backend.task.domain.board.Board;
import com.daon.backend.task.domain.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryImpl implements BoardRepository {

    private final BoardJpaRepository boardJpaRepository;

    @Override
    public Optional<Board> findBoardById(Long boardId) {
        return boardJpaRepository.findBoardById(boardId);
    }

    @Override
    public List<Board> findBoardsByProjectId(Long projectId) {
        return boardJpaRepository.findBoardsByProjectIdOrderByCreatedAtAsc(projectId);
    }
}
