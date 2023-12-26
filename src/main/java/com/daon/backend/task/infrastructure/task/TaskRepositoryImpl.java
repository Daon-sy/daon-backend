package com.daon.backend.task.infrastructure.task;

import com.daon.backend.common.history.Revision;
import com.daon.backend.task.domain.board.Board;
import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.project.QProjectParticipant;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.dto.*;
import com.daon.backend.task.dto.task.history.HistoryBoard;
import com.daon.backend.task.dto.task.history.HistoryProjectParticipant;
import com.daon.backend.task.dto.task.history.TaskHistory;
import com.daon.backend.task.infrastructure.board.BoardJpaRepository;
import com.daon.backend.task.infrastructure.project.ProjectParticipantJpaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.daon.backend.task.domain.board.QBoard.board;
import static com.daon.backend.task.domain.project.QProject.project;
import static com.daon.backend.task.domain.project.QProjectParticipant.projectParticipant;
import static com.daon.backend.task.domain.task.QTask.task;
import static com.daon.backend.task.domain.task.QTaskBookmark.taskBookmark;
import static com.daon.backend.task.domain.task.QTaskReply.taskReply;
import static com.querydsl.core.types.Projections.constructor;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final TaskJpaRepository taskJpaRepository;
    private final TaskBookmarkJpaRepository taskBookmarkJpaRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final ProjectParticipantJpaRepository projectParticipantJpaRepository;

    @Override
    public Task save(Task task) {
        return taskJpaRepository.save(task);
    }

    @Override
    public Optional<Task> findTaskById(Long taskId) {
        return taskJpaRepository.findTaskById(taskId);
    }

    @Override
    public boolean existsTaskBookmarkByTaskIdAndProjectParticipantId(Long taskId, Long projectParticipantId) {
        return taskBookmarkJpaRepository.existsTaskBookmarkByTaskIdAndParticipant_Id(taskId, projectParticipantId);
    }

    @Override
    public List<Task> findTasksForLessThanThreeDaysOld() {
        LocalDateTime currentDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime plusThreeDays = LocalDateTime.of(LocalDate.from(currentDate.plusDays(3)), LocalTime.MAX);

        return queryFactory
                .selectFrom(task)
                .where(task.endDate.between(currentDate, plusThreeDays).and(task.taskManager.isNotNull()))
                .orderBy(task.endDate.asc())
                .fetch();
    }

    @Override
    public List<TaskSummary> findTaskSummaries(String memberId, Long workspaceId, TaskSearchParams params) {
        BooleanBuilder builder = new BooleanBuilder();
        if (params.getProjectId() != null) {
            builder.and(task.board.project.id.eq(params.getProjectId()));
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

        QProjectParticipant qProjectParticipant = new QProjectParticipant("qP");
        return queryFactory
                .select(
                        constructor(
                                TaskSummary.class,
                                task.id,
                                constructor(
                                        ProjectSummary.class,
                                        task.board.project
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
                                taskBookmark.isNotNull(),
                                JPAExpressions.select(taskReply.count())
                                        .from(taskReply)
                                        .where(taskReply.task.id.eq(task.id)),
                                task.taskManager.memberId.eq(memberId)
                        )
                )
                .from(task)
                    .join(task.board, board)
                    .leftJoin(board.project, project)
                    .leftJoin(qProjectParticipant).on(qProjectParticipant.project.eq(project).and(qProjectParticipant.memberId.eq(memberId)))
                    .leftJoin(task.taskManager, projectParticipant)
                    .leftJoin(taskBookmark).on(task.eq(taskBookmark.task).and(taskBookmark.memberId.eq(memberId)))
                .where(project.workspace.id.eq(workspaceId).and(qProjectParticipant.isNotNull()).and(builder))
                .orderBy(task.emergency.desc(), task.modifiedAt.asc())
                .fetch();
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
                                                task.board.project
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
                        .join(task.board, board)
                        .leftJoin(board.project, project)
                        .leftJoin(task.taskManager, projectParticipant)
                        .leftJoin(taskBookmark).on(task.eq(taskBookmark.task).and(taskBookmark.memberId.eq(memberId)))
                        .where(task.id.eq(taskId))
                        .fetchOne()
        );
    }

    @Override
    public Slice<TaskHistory> findTaskHistoriesByProjectIdAndTaskId(Long projectId, Long taskId, Pageable pageable) {
        AuditQuery query = AuditReaderFactory.get(em).createQuery()
                .forRevisionsOfEntityWithChanges(Task.class, true)
                .add(AuditEntity.id().eq(taskId))
                .addOrder(AuditEntity.revisionNumber().desc())
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize() + 2)
                ;

        List<TaskHistory> taskHistories = new ArrayList<>();

        List<Object[]> results = query.getResultList();
        Object[] lastHistory = null;
        for (Object[] prevData : results) {
            if (lastHistory != null) {
                taskHistories.add(generateTaskHistory(lastHistory, prevData, projectId));
            }
            lastHistory = prevData;
        }

        boolean hasNext = false;
        if (taskHistories.size() > pageable.getPageSize()) {
            taskHistories.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(taskHistories, pageable, hasNext);
    }

    private TaskHistory generateTaskHistory(Object[] currentHistory, Object[] prevHistory, Long projectId) {
        Revision revision = (Revision) currentHistory[1];
        Set<String> modifiedFields = (Set<String>) currentHistory[3];
        String modifiedField = modifiedFields.stream().findFirst().orElseThrow();

        Task task = (Task) currentHistory[0];
        String modifiedMemberId = task.getModifiedBy();
        ProjectParticipant modifier = projectParticipantJpaRepository.findProjectParticipantByProjectIdAndMemberId(projectId, modifiedMemberId).orElseThrow();

        Task prevTask = (Task) prevHistory[0];

        TaskHistory taskHistory;
        try {
            Field field = task.getClass().getDeclaredField(modifiedField);
            field.setAccessible(true);
            Object fieldData = field.get(task);
            Object prevFieldData = field.get(prevTask);
            Class<?> fieldType = field.getType();
            if (fieldType.equals(Board.class)) {
                Long boardId = ((Board) fieldData).getId();
                Long prevBoardId = ((Board) prevFieldData).getId();

                Board board = boardJpaRepository.findById(boardId).orElseThrow();
                Board prevBoard = boardJpaRepository.findById(prevBoardId).orElseThrow();

                taskHistory = new TaskHistory(
                        revision.getRev(),
                        modifiedField,
                        fieldType.getSimpleName(),
                        new HistoryBoard(
                                prevBoard.getId(),
                                prevBoard.getTitle()
                        ),
                        new HistoryBoard(
                                board.getId(),
                                board.getTitle()
                        ),
                        new HistoryProjectParticipant(
                                modifier.getId(),
                                modifier.getWorkspaceParticipant().getProfile().getName(),
                                modifier.getWorkspaceParticipant().getProfile().getImageUrl()
                        ),
                        task.getModifiedAt()
                );
            }
            else if (fieldType.equals(ProjectParticipant.class)) {
                HistoryProjectParticipant from = null;
                HistoryProjectParticipant to = null;
                if (prevFieldData != null) {
                    Long prevTaskManagerId = ((ProjectParticipant) prevFieldData).getId();
                    ProjectParticipant prevTaskManager = projectParticipantJpaRepository.findById(prevTaskManagerId).orElseThrow();
                    from = new HistoryProjectParticipant(
                            prevTaskManager.getId(),
                            prevTaskManager.getWorkspaceParticipant().getProfile().getName(),
                            prevTaskManager.getWorkspaceParticipant().getProfile().getImageUrl()
                    );
                }

                if (fieldData != null) {
                    Long taskManagerId = ((ProjectParticipant) fieldData).getId();
                    ProjectParticipant taskManager = projectParticipantJpaRepository.findById(taskManagerId).orElseThrow();
                    to = new HistoryProjectParticipant(
                            taskManager.getId(),
                            taskManager.getWorkspaceParticipant().getProfile().getName(),
                            taskManager.getWorkspaceParticipant().getProfile().getImageUrl()
                    );
                }

                taskHistory = new TaskHistory(
                        revision.getRev(),
                        modifiedField,
                        fieldType.getSimpleName(),
                        from,
                        to,
                        new HistoryProjectParticipant(
                                modifier.getId(),
                                modifier.getWorkspaceParticipant().getProfile().getName(),
                                modifier.getWorkspaceParticipant().getProfile().getImageUrl()
                        ),
                        task.getModifiedAt()
                );
            }
            else if (fieldType.equals(LocalDateTime.class)) {
                taskHistory = new TaskHistory(
                        revision.getRev(),
                        modifiedField,
                        "Date",
                        prevFieldData == null ? null : ((LocalDateTime) prevFieldData).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        fieldData == null ? null : ((LocalDateTime) fieldData).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        new HistoryProjectParticipant(
                                modifier.getId(),
                                modifier.getWorkspaceParticipant().getProfile().getName(),
                                modifier.getWorkspaceParticipant().getProfile().getImageUrl()
                        ),
                        task.getModifiedAt()
                );
            }
            else {
                taskHistory = new TaskHistory(
                        revision.getRev(),
                        modifiedField,
                        fieldType.getSimpleName(),
                        prevFieldData,
                        fieldData,
                        new HistoryProjectParticipant(
                                modifier.getId(),
                                modifier.getWorkspaceParticipant().getProfile().getName(),
                                modifier.getWorkspaceParticipant().getProfile().getImageUrl()
                        ),
                        task.getModifiedAt()
                );
            }
            return taskHistory;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
