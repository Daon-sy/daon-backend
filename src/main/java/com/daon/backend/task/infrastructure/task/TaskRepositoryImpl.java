package com.daon.backend.task.infrastructure.task;

import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.dto.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.daon.backend.task.domain.project.QBoard.board;
import static com.daon.backend.task.domain.project.QProject.project;
import static com.daon.backend.task.domain.project.QProjectParticipant.projectParticipant;
import static com.daon.backend.task.domain.task.QTask.task;
import static com.daon.backend.task.domain.task.QTaskBookmark.taskBookmark;
import static com.querydsl.core.types.Projections.*;

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
        return taskJpaRepository.findByIdAndRemovedFalse(taskId);
    }

    @Override
    public List<Task> findTasksByProjectId(Long projectId) {
        return taskJpaRepository.findTasksByProjectIdAndRemovedFalse(projectId);
    }

    @Override
    public List<Task> findAllTasksByProjectId(Long projectId) {
        return taskJpaRepository.findAllTasksByProjectId(projectId);
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
                        constructor(
                                TaskSummary.class,
                                task.id,
                                constructor(
                                        ProjectSummary.class,
                                        task.project
                                ),
                                constructor(
                                        BoardSummary.class,
                                        task.board
                                ),
                                constructor(
                                        TaskManager.class,
                                        task.taskManager
                                ).skipNulls(),
                                task.title,
                                task.startDate,
                                task.endDate,
                                task.progressStatus,
                                task.emergency,
                                taskBookmark.isNotNull()
                        )
                )
                .from(task)
                    .join(task.project, project)
                    .leftJoin(task.board, board)
                    .leftJoin(task.taskManager, projectParticipant)
                    .leftJoin(taskBookmark).on(task.eq(taskBookmark.task).and(taskBookmark.memberId.eq(memberId)))
                .where(builder.and(task.removed.isFalse()))
                .fetch();
    }

    @Override
    public Slice<TaskSearchResult> searchTaskSummariesByTitle(String memberId, String title, Pageable pageable) {
        final int pageSize = pageable.getPageSize();
        final long offset = pageable.getOffset();

        List<TaskSearchResult> taskSearchResults = queryFactory
                .select(
                        constructor(
                                TaskSearchResult.class,
                                task.id,
                                constructor(
                                        ProjectSummary.class,
                                        task.project
                                ),
                                constructor(
                                        TaskManager.class,
                                        task.taskManager
                                ),
                                task.title,
                                task.startDate,
                                task.endDate,
                                task.progressStatus,
                                task.emergency
                        )
                )
                .from(task)
                .innerJoin(task.project.participants, projectParticipant)
                .where(task.title.contains(title)
                        .and(projectParticipant.memberId.eq(memberId))
                        .and(task.removed.isFalse()))
                .orderBy(task.modifiedAt.desc())
                .offset(offset)
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = false;
        if (taskSearchResults.size() > pageSize) {
            taskSearchResults.remove(pageSize);
            hasNext = true;
        }

        return new SliceImpl<>(taskSearchResults, pageable, hasNext);
    }

    @Override
    public Optional<TaskDetail> findTaskDetail(String memberId, Long taskId) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                constructor(
                                        TaskDetail.class,
                                        task.id,
                                        constructor(
                                                ProjectSummary.class,
                                                task.project
                                        ),
                                        constructor(
                                                BoardSummary.class,
                                                task.board
                                        ),
                                        constructor(
                                                TaskManager.class,
                                                task.taskManager
                                        ).skipNulls(),
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
                            .leftJoin(task.board, board)
                            .leftJoin(task.taskManager, projectParticipant)
                            .leftJoin(taskBookmark).on(task.eq(taskBookmark.task).and(taskBookmark.memberId.eq(memberId)))
                        .where(task.id.eq(taskId)
                                .and(task.removed.isFalse()))
                        .fetchOne()
        );
    }

    @Override
    public List<Task> findTasksByProjectIdAndBoardId(Long projectId, Long boardId) {
        return taskJpaRepository.findTasksByProjectIdAndBoardIdAndRemovedFalse(projectId, boardId);
    }

}
