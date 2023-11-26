package com.daon.backend.task.infrastructure;

import com.daon.backend.notification.domain.NotificationType;
import com.daon.backend.notification.infrastructure.NotificationService;
import com.daon.backend.task.domain.board.Board;
import com.daon.backend.task.domain.project.DeportationProjectAlarmEvent;
import com.daon.backend.task.domain.project.InviteProjectAlarmEvent;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.task.*;
import com.daon.backend.task.domain.workspace.DeportationWorkspaceAlarmEvent;
import com.daon.backend.task.domain.workspace.InviteWorkspaceAlarmEvent;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.dto.notification.DesignatedManagerAlarmResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class SendEventHandler {

    private final NotificationService notificationService;

    @TransactionalEventListener
    @Async
    public void handle(InviteWorkspaceAlarmEvent event) throws JsonProcessingException {
        notificationService.sendAlarm(NotificationType.INVITE_WORKSPACE, event.getData(), event.getMemberId());
    }

    @TransactionalEventListener
    @Async
    public void handle(DeportationWorkspaceAlarmEvent event) throws JsonProcessingException {
        notificationService.sendAlarm(NotificationType.DEPORTATION_WORKSPACE, event.getData(), event.getMemberId());
    }

    @TransactionalEventListener
    @Async
    public void handle(InviteProjectAlarmEvent event) throws JsonProcessingException {
        notificationService.sendAlarm(NotificationType.INVITE_PROJECT, event.getData(), event.getMemberId());
    }

    @TransactionalEventListener
    @Async
    public void handle(DeportationProjectAlarmEvent event) throws JsonProcessingException {
        notificationService.sendAlarm(NotificationType.DEPORTATION_PROJECT, event.getData(), event.getMemberId());
    }

    @TransactionalEventListener
    @Async
    public void handle(DesignatedManagerAlarmEvent event) throws JsonProcessingException {
        notificationService.sendAlarm(NotificationType.REGISTERED_TASK_MANAGER, event.getData(), event.getMemberId());
    }

    @TransactionalEventListener
    @Async
    public void handle(SendFindTaskEvent event) {
        notificationService.sendFindTaskEventNotification(event.getTaskId());
    }

    @TransactionalEventListener
    @Async
    public void handle(SendFindTasksEvent event) {
        notificationService.sendFindTasksEventNotification(
                event.getWorkspaceId(), event.getProjectId(), event.getBoardId()
        );
    }

    @TransactionalEventListener
    @Async
    public void handleTaskCreatedEvent(TaskCreatedEvent event) throws Exception {
        Task task = event.getTask();
        notifyTaskListUpdated(task);

        Project project = task.getProject();
        Workspace workspace = project.getWorkspace();
        notificationService.sendAlarm(
                NotificationType.REGISTERED_TASK_MANAGER,
                new DesignatedManagerAlarmResponseDto(
                        workspace.getId(),
                        workspace.getTitle(),
                        project.getId(),
                        project.getTitle(),
                        task.getId(),
                        task.getTitle()
                ),
                task.getTaskManager().getMemberId()
        );
    }

    private void notifyTaskListUpdated(Task task) {
        Project project = task.getProject();
        Workspace workspace = project.getWorkspace();
        Board board = task.getBoard();
        notificationService.sendFindTasksEventNotification(
                workspace.getId(),
                project.getId(),
                board.getId()
        );
    }

    @TransactionalEventListener
    @Async
    public void handleTaskRemovedEvent(TaskRemovedEvent event) {
        Task task = event.getTask();
        notifyTaskListUpdated(task);
    }
}
