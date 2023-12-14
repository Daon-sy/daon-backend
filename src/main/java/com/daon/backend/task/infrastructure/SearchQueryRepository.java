package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.dto.BoardSummary;
import com.daon.backend.task.dto.ProjectSummary;
import com.daon.backend.task.dto.TaskManager;
import com.daon.backend.task.dto.WorkspaceSummary;
import com.daon.backend.task.dto.search.SearchResponseDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.daon.backend.task.domain.board.QBoard.board;
import static com.daon.backend.task.domain.project.QProject.project;
import static com.daon.backend.task.domain.project.QProjectParticipant.projectParticipant;
import static com.daon.backend.task.domain.task.QTask.task;
import static com.daon.backend.task.domain.task.QTaskBookmark.taskBookmark;
import static com.daon.backend.task.domain.task.QTaskReply.taskReply;
import static com.daon.backend.task.domain.workspace.QWorkspace.workspace;
import static com.daon.backend.task.domain.workspace.QWorkspaceParticipant.workspaceParticipant;

@Repository
@RequiredArgsConstructor
public class SearchQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final SecuritySessionMemberProvider sessionMemberProvider;

    public Page<SearchResponseDto.WorkspaceResult> findWorkspacesByTitle(String keyword, Pageable pageable) {
        String memberId = sessionMemberProvider.getMemberId();

        List<SearchResponseDto.WorkspaceResult> result = queryFactory
                .select(
                        Projections.constructor(
                                SearchResponseDto.WorkspaceResult.class,
                                workspace,
                                workspace.participants.size(),
                                workspace.createdAt,
                                workspace.modifiedAt
                        )
                )
                .from(workspace)
                    .join(workspaceParticipant).on(
                        workspaceParticipant.workspace.eq(workspace).and(
                            workspaceParticipant.memberId.eq(memberId))
                    )
                .where(workspace.title.containsIgnoreCase(keyword))
                .orderBy(
                        pageable.getSort().stream()
                                .map(order -> new OrderSpecifier(
                                        order.isAscending() ? Order.ASC : Order.DESC,
                                        new PathBuilder(Workspace.class, "workspace").get(order.getProperty())
                                ))
                                .toArray(OrderSpecifier[]::new)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        Long totalCount = queryFactory.select(workspace.count())
                .from(workspace)
                    .join(workspaceParticipant).on(
                            workspaceParticipant.workspace.eq(workspace).and(
                                    workspaceParticipant.memberId.eq(memberId))
                    )
                .where(workspace.title.containsIgnoreCase(keyword))
                .distinct()
                .fetchFirst();

        return new PageImpl<>(result, pageable, totalCount);
    }

    public Page<SearchResponseDto.ProjectResult> findProjectsByTitle(String keyword, Pageable pageable) {
        String memberId = sessionMemberProvider.getMemberId();

        List<SearchResponseDto.ProjectResult> result = queryFactory
                .select(
                        Projections.constructor(
                                SearchResponseDto.ProjectResult.class,
                                project.id,
                                project.title,
                                project.description,
                                project.participants.size(),
                                Projections.constructor(
                                        WorkspaceSummary.class,
                                        workspace
                                ),
                                project.createdAt,
                                project.modifiedAt
                        )
                )
                .from(project)
                    .join(projectParticipant).on(
                            projectParticipant.project.eq(project).and(
                                    projectParticipant.memberId.eq(memberId)
                            )
                    )
                    .join(project.workspace, workspace)
                .where(project.title.containsIgnoreCase(keyword))
                .orderBy(
                        pageable.getSort().stream()
                                .map(order -> new OrderSpecifier(
                                        order.isAscending() ? Order.ASC : Order.DESC,
                                        new PathBuilder(Project.class, "project").get(order.getProperty())
                                ))
                                .toArray(OrderSpecifier[]::new)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        Long totalCount = queryFactory
                .select(project.count())
                .from(project)
                .join(projectParticipant).on(
                        projectParticipant.project.eq(project).and(
                                projectParticipant.memberId.eq(memberId)
                        )
                )
                .where(project.title.containsIgnoreCase(keyword))
                .fetchFirst();

        return new PageImpl<>(result, pageable, totalCount);
    }

    public Page<SearchResponseDto.TaskResult> findTasksByTitle(String keyword, Pageable pageable) {
        String memberId = sessionMemberProvider.getMemberId();

        List<SearchResponseDto.TaskResult> result = queryFactory
                .select(
                        Projections.constructor(
                                SearchResponseDto.TaskResult.class,
                                task.id,
                                Projections.constructor(
                                        ProjectSummary.class,
                                        task.board.project
                                ),
                                Projections.constructor(
                                        BoardSummary.class,
                                        task.board
                                ),
                                Projections.constructor(
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
                                task.taskManager.memberId.eq(memberId),
                                Projections.constructor(
                                        WorkspaceSummary.class,
                                        workspace
                                ),
                                task.createdAt,
                                task.modifiedAt
                        )
                )
                .from(task)
                .join(task.board, board)
                .join(board.project, project)
                .join(projectParticipant).on(
                        projectParticipant.project.eq(project).and(
                                projectParticipant.memberId.eq(memberId)
                        )
                )
                .join(project.workspace, workspace)
                .join(task.board, board)
                .leftJoin(task.taskManager, projectParticipant)
                .leftJoin(taskBookmark).on(task.eq(taskBookmark.task).and(taskBookmark.memberId.eq(memberId)))
                .where(task.title.containsIgnoreCase(keyword))
                .orderBy(
                        pageable.getSort().stream()
                                .map(order -> new OrderSpecifier(
                                        order.isAscending() ? Order.ASC : Order.DESC,
                                        new PathBuilder(Task.class, "task").get(order.getProperty())
                                ))
                                .toArray(OrderSpecifier[]::new)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory
                .select(task.count())
                .from(task)
                    .join(task.board.project, project)
                    .join(projectParticipant).on(
                            projectParticipant.project.eq(project).and(
                                    projectParticipant.memberId.eq(memberId)
                            )
                    )
                .where(task.title.containsIgnoreCase(keyword))
                .fetchFirst();

        return new PageImpl<>(result, pageable, totalCount);
    }
}
