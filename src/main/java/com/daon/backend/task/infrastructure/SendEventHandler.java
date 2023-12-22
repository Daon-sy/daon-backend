package com.daon.backend.task.infrastructure;

import com.daon.backend.notification.domain.Notification;
import com.daon.backend.notification.domain.data.Message;
import com.daon.backend.notification.infrastructure.NotificationSseService;
import com.daon.backend.task.domain.project.DeportationProjectAlarmEvent;
import com.daon.backend.task.domain.project.InviteProjectAlarmEvent;
import com.daon.backend.task.domain.workspace.DeportationWorkspaceAlarmEvent;
import com.daon.backend.task.domain.workspace.InviteWorkspaceAlarmEvent;
import com.daon.backend.task.domain.workspace.SendReceiveMessageAlarmEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class SendEventHandler {

    private final NotificationSseService notificationSseService;

    @Async
    @TransactionalEventListener
    public void handle(InviteWorkspaceAlarmEvent event) {
        notificationSseService.sendAlarm(
                Notification.inviteWorkspace(event.getMemberId(), new com.daon.backend.notification.domain.data.Workspace(
                        event.getData().getWorkspace().getWorkspaceId(),
                        event.getData().getWorkspace().getWorkspaceTitle()
                ))
        );
    }

    @Async
    @TransactionalEventListener
    public void handle(DeportationWorkspaceAlarmEvent event) {
        notificationSseService.sendAlarm(
                Notification.deportationWorkspace(event.getMemberId(), new com.daon.backend.notification.domain.data.Workspace(
                        event.getData().getWorkspace().getWorkspaceId(),
                        event.getData().getWorkspace().getWorkspaceTitle()
                ))
        );
    }

    @Async
    @TransactionalEventListener
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

    @Async
    @TransactionalEventListener
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

    @Async
    @TransactionalEventListener
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
}
