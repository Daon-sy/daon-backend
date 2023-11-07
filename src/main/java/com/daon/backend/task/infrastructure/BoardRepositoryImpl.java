package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.Board;
import com.daon.backend.task.domain.project.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryImpl implements BoardRepository {

    private final BoardJpaRepository boardJpaRepository;

    @Override
    public Board save(Board board) {
        return boardJpaRepository.save(board);
    }

    @Override
    public boolean existsBoardByTitle(String title) {
        return boardJpaRepository.existsBoardByTitle(title);
    }

    @Override
    public List<Board> findBoardsByWorkspaceIdAndProjectId(Long workspaceId, Long projectId) {
        return boardJpaRepository.findBoardsByWorkspaceIdAndProjectId(workspaceId, projectId);
    }
}