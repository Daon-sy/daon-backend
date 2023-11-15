package com.daon.backend.task.infrastructure.project;

import com.daon.backend.task.domain.project.Board;
import com.daon.backend.task.domain.project.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryImpl implements BoardRepository {

    private final BoardJpaRepository boardJpaRepository;


    @Override
    public boolean existsBoardByTitle(String title) {
        return boardJpaRepository.existsBoardByTitle(title);
    }

    @Override
    public Optional<Board> findBoardByProjectId(Long projectId) {
        return boardJpaRepository.findBoardByProjectId(projectId);
    }

}
