package com.daon.backend.task.infrastructure;

import com.daon.backend.notification.domain.Notification;
import com.daon.backend.notification.domain.data.Message;
import com.daon.backend.notification.infrastructure.NotificationSseService;
import com.daon.backend.task.domain.board.Board;
import com.daon.backend.task.domain.project.DeportationProjectAlarmEvent;
import com.daon.backend.task.domain.project.InviteProjectAlarmEvent;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.task.*;
import com.daon.backend.task.domain.workspace.DeportationWorkspaceAlarmEvent;
import com.daon.backend.task.domain.workspace.InviteWorkspaceAlarmEvent;
import com.daon.backend.task.domain.workspace.SendReceiveMessageAlarmEvent;
import com.daon.backend.task.domain.workspace.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class SendEventHandler {

    private final NotificationSseService notificationSseService;

    @TransactionalEventListener
    @Async
    public void handle(InviteWorkspaceAlarmEvent event) {
        notificationSseService.sendAlarm(
                Notification.inviteWorkspace(event.getMemberId(), new com.daon.backend.notification.domain.data.Workspace(
                        event.getData().getWorkspace().getWorkspaceId(),
                        event.getData().getWorkspace().getWorkspaceTitle()
                ))
        );
    }

    @TransactionalEventListener
    @Async
    public void handle(DeportationWorkspaceAlarmEvent event) {
        notificationSseService.sendAlarm(
                Notification.deportationWorkspace(event.getMemberId(), new com.daon.backend.notification.domain.data.Workspace(
                        event.getData().getWorkspace().getWorkspaceId(),
                        event.getData().getWorkspace().getWorkspaceTitle()
                ))
        );
    }

    @TransactionalEventListener
    @Async
    public void handle(InviteProjectAlarmEvent event) {
        notificationSseService.sendAlarm(
                Notification.inviteProject(
                        event.getMemberId(),
                        new com.daon.backend.notification.domain.data.Workspace(
                                event.getData().getWorkspace().getWorkspaceId(),
                                event.getData().getWorkspace().getWorkspaceTitle()
                        ),
                        new com.daon.backend.notification.domain.data.Project(
                                event.getData().getProject().getProjectId(),
                                event.getData().getProject().getProjectTitle()
                        )
                )
        );
    }

    @TransactionalEventListener
    @Async
    public void handle(DeportationProjectAlarmEvent event) {
        notificationSseService.sendAlarm(
                Notification.deportationProject(
                        event.getMemberId(),
                        new com.daon.backend.notification.domain.data.Workspace(
                                event.getData().getWorkspace().getWorkspaceId(),
                                event.getData().getWorkspace().getWorkspaceTitle()
                        ),
                        new com.daon.backend.notification.domain.data.Project(
                                event.getData().getProject().getProjectId(),
                                event.getData().getProject().getProjectTitle()
                        )
                )
        );
    }

    @TransactionalEventListener
    @Async
    public void handle(DesignatedManagerAlarmEvent event) {
        notificationSseService.sendAlarm(
                Notification.registeredTaskManager(
                        event.getMemberId(),
                        new com.daon.backend.notification.domain.data.Workspace(
                                event.getData().getWorkspace().getWorkspaceId(),
                                event.getData().getWorkspace().getWorkspaceTitle()
                        ),
                        new com.daon.backend.notification.domain.data.Project(
                                event.getData().getProject().getProjectId(),
                                event.getData().getProject().getProjectTitle()
                        ),
                        new com.daon.backend.notification.domain.data.Task(
                                event.getData().getTask().getTaskId(),
                                event.getData().getTask().getTaskTitle()
                        )
                )
        );
    }


    @TransactionalEventListener
    @Async
    public void handle(SendReceiveMessageAlarmEvent event) {
        notificationSseService.sendAlarm(
                Notification.receiveMessage(
                        event.getMemberId(),
                        new com.daon.backend.notification.domain.data.Workspace(
                                event.getData().getWorkspace().getWorkspaceId(),
                                event.getData().getWorkspace().getWorkspaceTitle()
                        ),
                        new Message(
                                event.getData().getMessageId(),
                                event.getData().getSender().getWorkspaceParticipantId(),
                                event.getData().getSender().getName(),
                                event.getData().getSender().getEmail(),
                                event.getData().getSender().getImageUrl()
                        )
                )
        );
    }

    @TransactionalEventListener
    @Async
    public void handle(SendFindTaskEvent event) {
        notificationSseService.sendFindTaskEventNotification(event.getTaskId());
    }

    @TransactionalEventListener
    @Async
    public void handle(SendFindTasksEvent event) {
        notificationSseService.sendFindTasksEventNotification(
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
        notificationSseService.sendAlarm(
                Notification.registeredTaskManager(
                        task.getTaskManager().getMemberId(),
                        new com.daon.backend.notification.domain.data.Workspace(
                                workspace.getId(),
                                workspace.getTitle()
                        ),
                        new com.daon.backend.notification.domain.data.Project(
                                project.getId(),
                                project.getTitle()
                        ),
                        new com.daon.backend.notification.domain.data.Task(
                                task.getId(),
                                task.getTitle()
                        )
                )
        );
    }

    private void notifyTaskListUpdated(Task task) {
        Project project = task.getProject();
        Workspace workspace = project.getWorkspace();
        Board board = task.getBoard();
        notificationSseService.sendFindTasksEventNotification(
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
