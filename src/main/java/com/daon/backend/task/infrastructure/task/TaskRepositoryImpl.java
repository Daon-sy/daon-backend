package com.daon.backend.task.infrastructure.task;

import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.dto.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.daon.backend.task.domain.project.QBoard.board;
import static com.daon.backend.task.domain.project.QProject.project;
import static com.daon.backend.task.domain.project.QProjectParticipant.projectParticipant;
import static com.daon.backend.task.domain.task.QTask.task;
import static com.daon.backend.task.domain.task.QTaskBookmark.taskBookmark;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final JPAQueryFactory queryFactory;
    private final TaskJpaRepository taskJpaRepository;
    private final TaskBookmarkJpaRepository taskBookmarkJpaRepository;

    @Override
    public Task save(Task task) {
        return taskJpaRepository.save(task);
    }

    @Override
    public Optional<Task> findTaskByTaskId(Long taskId) {
        return taskJpaRepository.findById(taskId);
    }

    @Override
    public boolean existsTaskBookmarkByTaskIdAndProjectParticipantId(Long taskId, Long projectParticipantId) {
        return taskBookmarkJpaRepository.existsTaskBookmarkByTaskIdAndParticipant_Id(taskId, projectParticipantId);
    }

    @Override
    public List<TaskSummary> findTaskSummaries(String memberId, TaskSearchParams params) {
        BooleanBuilder builder = new BooleanBuilder();
        if (params.getProjectId() != null) {
            builder.and(task.project.id.eq(params.getProjectId()));
        }

        if (params.getBoardId() != null) {
            builder.and(task.board.id.eq(params.getBoardId()));
        }

        if (params.isBookmarked()) {
            builder.and(taskBookmark.isNotNull());
        }

        if (params.isMy()) {
            builder.and(task.taskManager.memberId.eq(memberId));
        }

        return queryFactory
                .select(
                        Projections.constructor(
                                TaskSummary.class,
                                task.id,
                                Projections.constructor(
                                        ProjectSummary.class,
                                        task.project
                                ),
                                Projections.constructor(
                                        BoardSummary.class,
                                        task.board
                                ),
                                Projections.constructor(
                                        TaskManager.class,
                                        task.taskManager
                                ),
                                task.title,
                                task.startDate,
                                task.endDate,
                                task.progressStatus,
                                task.emergency,
                                taskBookmark.isNotNull()
                        )
                )
                .from(task).leftJoin(taskBookmark).on(task.eq(taskBookmark.task).and(taskBookmark.memberId.eq(memberId)))
                .where(builder)
                .fetch();
    }

    @Override
    public Optional<TaskDetail> findTaskDetail(String memberId, Long taskId) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                Projections.constructor(
                                        TaskDetail.class,
                                        task.id,
                                        Projections.constructor(
                                                ProjectSummary.class,
                                                task.project
                                        ),
                                        Projections.constructor(
                                                BoardSummary.class,
                                                task.board
                                        ),
                                        Projections.constructor(
                                                TaskManager.class,
                                                task.taskManager
                                        ),
                                        task.title,
                                        task.content,
                                        task.startDate,
                                        task.endDate,
                                        task.progressStatus,
                                        task.emergency,
                                        taskBookmark.isNotNull(),
                                        task.createdAt,
                                        task.modifiedAt
                                )
                        )
                        .from(task)
                            .join(task.project, project)
                            .join(task.board, board)
                            .join(task.taskManager, projectParticipant)
                            .leftJoin(taskBookmark).on(task.eq(taskBookmark.task).and(taskBookmark.memberId.eq(memberId)))
                        .where(task.id.eq(taskId))
                        .fetchOne()
        );
    }

}
