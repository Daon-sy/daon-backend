package com.daon.backend.notification.domain;

import com.daon.backend.config.BaseEntity;
import com.daon.backend.notification.domain.data.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@DiscriminatorColumn(name = "d_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected NotificationType notificationType;

    protected String targetMemberId;

    protected long whenEventPublished;

    protected boolean read;

    public void doRead() {
        this.read = true;
    }

    protected Notification(NotificationType notificationType, String targetMemberId, long whenEventPublished) {
        this.notificationType = notificationType;
        this.targetMemberId = targetMemberId;
        this.whenEventPublished = whenEventPublished;

        this.read = false;
    }

    public static Notification deportationProject(String targetMemberId, Workspace workspace, Project project) {
        return new DeportationProjectNotification(targetMemberId, workspace, project);
    }

    public static Notification deportationWorkspace(String targetMemberId, Workspace workspace) {
        return new DeportationWorkspaceNotification(targetMemberId, workspace);
    }

    public static Notification inviteWorkspace(String targetMemberId, Workspace workspace) {
        return new InvitedWorkspaceNotification(targetMemberId, workspace);
    }

    public static Notification inviteProject(String targetMemberId, Workspace workspace, Project project) {
        return new InvitedProjectNotification(targetMemberId, workspace, project);
    }

    public static Notification registeredTaskManager(String targetMemberId, Workspace workspace, Project project, Board board, Task task) {
        return new RegisteredTaskManagerNotification(targetMemberId, workspace, project, board, task);
    }

    public static Notification receiveMessage(String targetMemberId, Workspace workspace, Message message) {
        return new ReceiveMessageNotification(targetMemberId, workspace, message);
    }

    @Entity @Getter
    @DiscriminatorValue("DEPORTATION_PROJECT")
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DeportationProjectNotification extends Notification {

        @Embedded
        private Workspace workspace;

        @Embedded
        private Project project;

        protected DeportationProjectNotification(String targetMemberId, Workspace workspace, Project project) {
            super(NotificationType.DEPORTATION_PROJECT, targetMemberId, System.currentTimeMillis());
            this.workspace = workspace;
            this.project = project;
        }
    }

    @Entity @Getter
    @DiscriminatorValue("DEPORTATION_WORKSPACE")
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DeportationWorkspaceNotification extends Notification {

        @Embedded
        private Workspace workspace;

        protected DeportationWorkspaceNotification(String targetMemberId, Workspace workspace) {
            super(NotificationType.DEPORTATION_WORKSPACE, targetMemberId, System.currentTimeMillis());
            this.workspace = workspace;
        }
    }

    @Entity @Getter
    @DiscriminatorValue("INVITE_PROJECT")
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class InvitedProjectNotification extends Notification {

        @Embedded
        private Workspace workspace;

        @Embedded
        private Project project;

        protected InvitedProjectNotification(
                String targetMemberId,
                Workspace workspace,
                Project project
        ) {
            super(NotificationType.INVITE_PROJECT, targetMemberId, System.currentTimeMillis());
            this.workspace = workspace;
            this.project = project;
        }
    }

    @Entity @Getter
    @DiscriminatorValue("INVITE_WORKSPACE")
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class InvitedWorkspaceNotification extends Notification {

        @Embedded
        private Workspace workspace;

        protected InvitedWorkspaceNotification(
                String targetMemberId,
                Workspace workspace
        ) {
            super(NotificationType.INVITE_WORKSPACE, targetMemberId, System.currentTimeMillis());
            this.workspace = workspace;
        }
    }

    @Entity @Getter
    @DiscriminatorValue("REGISTERED_TASK_MANAGER")
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RegisteredTaskManagerNotification extends Notification {

        @Embedded
        private Workspace workspace;

        @Embedded
        private Project project;

        @Embedded
        private Board board;

        @Embedded
        private Task task;

        protected RegisteredTaskManagerNotification(String targetMemberId, Workspace workspace, Project project, Board board, Task task) {
            super(NotificationType.REGISTERED_TASK_MANAGER, targetMemberId, System.currentTimeMillis());
            this.workspace = workspace;
            this.project = project;
            this.board = board;
            this.task = task;
        }
    }

    @Entity @Getter
    @DiscriminatorValue("RECEIVE_MESSAGE")
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReceiveMessageNotification extends Notification {

        @Embedded
        private Workspace workspace;

        @Embedded
        private Message message;

        protected ReceiveMessageNotification(String targetMemberId, Workspace workspace, Message message) {
            super(NotificationType.RECEIVE_MESSAGE, targetMemberId, System.currentTimeMillis());
            this.workspace = workspace;
            this.message = message;
        }
    }
}
