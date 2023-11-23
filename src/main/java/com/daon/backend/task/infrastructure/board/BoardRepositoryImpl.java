package com.daon.backend.task.infrastructure.board;

import com.daon.backend.task.domain.board.Board;
import com.daon.backend.task.domain.board.BoardRepository;
import com.daon.backend.task.domain.project.ProjectParticipant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.daon.backend.task.domain.task.QTask.task;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryImpl implements BoardRepository {

    private final BoardJpaRepository boardJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public Optional<Board> findBoardById(Long boardId) {
        return boardJpaRepository.findBoardByIdAndRemovedFalse(boardId);
    }

    @Override
    public void deleteTasksRelatedBoard(Long boardId) {
        queryFactory.update(task)
                .set(task.taskManager, (ProjectParticipant) null)
                .set(task.creatorId, (Long) null)
                .set(task.removed, true)
                .where(task.board.id.eq(boardId))
                .execute();

        em.flush();
        em.clear();
    }
}
