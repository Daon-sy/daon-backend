package com.daon.backend.task.infrastructure;

import com.daon.backend.notification.domain.Notification;
import com.daon.backend.notification.infrastructure.NotificationSseService;
import com.daon.backend.task.domain.board.Board;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskEvent;
import com.daon.backend.task.domain.workspace.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TaskEventHandler {

    private final NotificationSseService notificationSseService;

    @Async
    @TransactionalEventListener
    public void handleCreatedTask(TaskEvent.Created event) {
        Task task = event.getTask();
        notifyTaskListUpdated(task);
    }

    // 해당 Task 포함된 프로젝트 페이지에 Task 변경 알림 전송
    private void notifyTaskListUpdated(Task task) {
        Project project = task.getBoard().getProject();
        Workspace workspace = project.getWorkspace();
        Board board = task.getBoard();
        notificationSseService.sendFindTasksEventNotification(
                workspace.getId(),
                project.getId(),
                board.getId()
        );
    }

    @Async
    @TransactionalEventListener
    public void handleModifiedTask(TaskEvent.Modified event) {
        Task task = event.getTask();
        notifyTaskListUpdated(task);
        notifyTaskDetailUpdated(task);
    }

    // 해당 할 일을 보고있는 페이지에 할 일 데이터 전송
    private void notifyTaskDetailUpdated(Task task) {
        notificationSseService.sendFindTaskEventNotification(task.getId());
    }

    @Async
    @TransactionalEventListener
    public void handleRemovedTask(TaskEvent.Removed event) {
        Task task = event.getTask();
        notifyTaskListUpdated(task);
    }

    @Async
    @TransactionalEventListener
    public void handleAssigned(TaskEvent.Assigned event) {
        Task task = event.getTask();
        sendMessageToTaskManager(task);
    }

    // 담당자에게 알림 전송
    private void sendMessageToTaskManager(Task task) {
        ProjectParticipant taskManager = task.getTaskManager();
        if (taskManager != null) {
            Board board = task.getBoard();
            Project project = board.getProject();
            Workspace workspace = project.getWorkspace();

            notificationSseService.sendAlarm(
                    Notification.registeredTaskManager(
                            taskManager.getMemberId(),
                            new com.daon.backend.notification.domain.data.Workspace(
                                    workspace.getId(),
                                    workspace.getTitle()
                            ),
                            new com.daon.backend.notification.domain.data.Project(
                                    project.getId(),
                                    project.getTitle()
                            ),
                            new com.daon.backend.notification.domain.data.Board(
                                    board.getId(),
                                    board.getTitle()
                            ),
                            new com.daon.backend.notification.domain.data.Task(
                                    task.getId(),
                                    task.getTitle()
                            )
                    )
            );
        }
    }
}
