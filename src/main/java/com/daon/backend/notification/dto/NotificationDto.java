package com.daon.backend.notification.dto;

import com.daon.backend.notification.domain.Notification;
import com.daon.backend.notification.domain.NotificationType;
import com.daon.backend.notification.domain.data.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationDto {

    private Long notificationId;
    private NotificationData data;
    private NotificationType type;
    private boolean read;

    public NotificationDto(Long notificationId, NotificationData data, NotificationType type) {
        this.notificationId = notificationId;
        this.data = data;
        this.type = type;
    }

    public NotificationDto(Notification notification) {
        this.notificationId = notification.getId();
        this.type = notification.getNotificationType();
        this.read = notification.isRead();

        switch (this.type) {
            case REGISTERED_TASK_MANAGER: {
                Notification.RegisteredTaskManagerNotification noti = (Notification.RegisteredTaskManagerNotification) notification;
                this.data = NotificationData.builder()
                        .time(noti.getCreatedAt())
                        .workspace(new WorkspaceData(noti.getWorkspace()))
                        .project(new ProjectData(noti.getProject()))
                        .board(new BoardData(noti.getBoard()))
                        .task(new TaskData(noti.getTask()))
                        .build();
                break;
            }
            case INVITE_WORKSPACE: {
                Notification.InvitedWorkspaceNotification noti = (Notification.InvitedWorkspaceNotification) notification;
                this.data = NotificationData.builder()
                        .time(noti.getCreatedAt())
                        .workspace(new WorkspaceData(noti.getWorkspace()))
                        .build();
                break;
            }
            case INVITE_PROJECT: {
                Notification.InvitedProjectNotification noti = (Notification.InvitedProjectNotification) notification;
                this.data = NotificationData.builder()
                        .time(noti.getCreatedAt())
                        .workspace(new WorkspaceData(noti.getWorkspace()))
                        .project(new ProjectData(noti.getProject()))
                        .build();
                break;
            }
            case DEPORTATION_WORKSPACE: {
                Notification.DeportationWorkspaceNotification noti = (Notification.DeportationWorkspaceNotification) notification;
                this.data = NotificationData.builder()
                        .time(noti.getCreatedAt())
                        .workspace(new WorkspaceData(noti.getWorkspace()))
                        .build();
                break;
            }
            case DEPORTATION_PROJECT: {
                Notification.DeportationProjectNotification noti = (Notification.DeportationProjectNotification) notification;
                this.data = NotificationData.builder()
                        .time(noti.getCreatedAt())
                        .workspace(new WorkspaceData(noti.getWorkspace()))
                        .project(new ProjectData(noti.getProject()))
                        .build();
                break;
            }
            case RECEIVE_MESSAGE: {
                Notification.ReceiveMessageNotification noti = (Notification.ReceiveMessageNotification) notification;
                this.data = NotificationData.builder()
                        .time(noti.getCreatedAt())
                        .workspace(new WorkspaceData(noti.getWorkspace()))
                        .message(new MessageData(noti.getMessage()))
                        .build();
                break;
            }
            default:
                this.data = null;
        }
    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class NotificationData {

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime time;

        private WorkspaceData workspace;
        private ProjectData project;
        private BoardData board;
        private TaskData task;
        private MessageData message;

        @Builder
        public NotificationData(LocalDateTime time, WorkspaceData workspace, ProjectData project, BoardData board, TaskData task, MessageData message) {
            this.time = time;
            this.workspace = workspace;
            this.project = project;
            this.board = board;
            this.task = task;
            this.message = message;
        }
    }

    @Getter
    public static class WorkspaceData {
        private Long workspaceId;
        private String workspaceTitle;

        public WorkspaceData(Long workspaceId, String workspaceTitle) {
            this.workspaceId = workspaceId;
            this.workspaceTitle = workspaceTitle;
        }

        public WorkspaceData(Workspace workspace) {
            this.workspaceId = workspace.getId();
            this.workspaceTitle = workspace.getTitle();
        }
    }

    @Getter
    public static class ProjectData {
        private Long projectId;
        private String projectTitle;

        public ProjectData(Long projectId, String projectTitle) {
            this.projectId = projectId;
            this.projectTitle = projectTitle;
        }

        public ProjectData(Project project) {
            this.projectId = project.getId();
            this.projectTitle = project.getTitle();
        }
    }

    @Getter
    public static class BoardData {
        private Long boardId;
        private String boardTitle;

        public BoardData(Long boardId, String boardTitle) {
            this.boardId = boardId;
            this.boardTitle = boardTitle;
        }

        public BoardData(Board board) {
            this.boardId = board.getId();
            this.boardTitle = board.getTitle();
        }
    }

    @Getter
    public static class TaskData {
        private Long taskId;
        private String taskTitle;

        public TaskData(Long taskId, String taskTitle) {
            this.taskId = taskId;
            this.taskTitle = taskTitle;
        }

        public TaskData(Task task) {
            this.taskId = task.getId();
            this.taskTitle = task.getTitle();
        }
    }

    @Getter
    public static class MessageData {
        private Long messageId;
        private MessageSenderData sender;

        public MessageData(Message message) {
            this.messageId = message.getMessageId();
            this.sender = new MessageSenderData(message);
        }
    }

    @Getter
    public static class MessageSenderData {
        private Long workspaceParticipantId;
        private String name;
        private String email;
        private String imageUrl;

        public MessageSenderData(Message message) {
            this.workspaceParticipantId = message.getWorkspaceParticipantId();
            this.name = message.getName();
            this.email = message.getEmail();
            this.imageUrl = message.getImageUrl();
        }
    }
}
