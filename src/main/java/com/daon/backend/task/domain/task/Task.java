package com.daon.backend.task.domain.task;

import com.daon.backend.common.event.Events;
import com.daon.backend.config.BaseTimeEntity;
import com.daon.backend.notification.domain.NotificationType;
import com.daon.backend.notification.domain.SendNotificationEvent;
import com.daon.backend.notification.dto.response.DesignatedManagerResponseDto;
import com.daon.backend.task.domain.project.Board;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectParticipant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    private String title;

    private String content;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean emergency;

    @Enumerated(EnumType.STRING)
    private TaskProgressStatus progressStatus;

    // workspaceParticipantId
    private Long creatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_manager_id")
    private ProjectParticipant taskManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    private boolean removed;

    @OneToMany(mappedBy = "task", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<TaskBookmark> taskBookmarks = new ArrayList<>();

    @Builder
    public Task(String title, String content, LocalDateTime startDate, LocalDateTime endDate, boolean emergency,
                Long creatorId, ProjectParticipant taskManager, Project project, Board board) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.emergency = emergency;
        this.creatorId = creatorId;
        this.taskManager = taskManager;
        this.project = project;
        this.board = board;

        this.progressStatus = TaskProgressStatus.TODO;
    }

    public void modifyTask(String title, String content, LocalDateTime startDate, LocalDateTime endDate, boolean emergency,
                           TaskProgressStatus progressStatus, Board board, ProjectParticipant taskManager) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.emergency = emergency;
        this.progressStatus = progressStatus;
        this.board = board;
        this.taskManager = taskManager;

        DesignatedManagerResponseDto designatedManagerEventResponse = createDesignatedManagerEventResponse();
        Events.raise(SendNotificationEvent.create(
                NotificationType.REGISTERED_TASK_MANAGER, designatedManagerEventResponse, taskManager.getMemberId()
        ));
    }

    private DesignatedManagerResponseDto createDesignatedManagerEventResponse() {
        return new DesignatedManagerResponseDto(
                this.project.getWorkspace().getId(), this.project.getWorkspace().getTitle(),
                this.project.getId(), this.project.getTitle(), this.id, this.title);
    }

    public void addTaskBookmark(TaskBookmark taskBookmark) {
        this.taskBookmarks.add(taskBookmark);
    }

    public void removeTaskBookmark(ProjectParticipant projectParticipant) {
        this.taskBookmarks.removeIf(taskBookmark -> taskBookmark.getParticipant().equals(projectParticipant));
    }

    public void modifyProgressStatus(TaskProgressStatus progressStatus) {
        this.progressStatus = Optional.ofNullable(progressStatus).orElse(this.progressStatus);
    }

    public void removeTaskManager() {
        this.taskManager = null;
    }

    public void removeCreator() {
        this.creatorId = null;
    }

    public void removeTask() {
        this.removed = true;
        removeTaskManager();
        removeCreator();
    }

    public void removeTaskWhenBoardDeleted() {
        this.removed = true;
        removeTaskManager();
    }
}
