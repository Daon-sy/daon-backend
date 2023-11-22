package com.daon.backend.task.domain.task;

import com.daon.backend.common.event.Events;
import com.daon.backend.config.BaseEntity;
import com.daon.backend.notification.domain.NotificationType;
import com.daon.backend.notification.domain.SendAlarmEvent;
import com.daon.backend.notification.domain.SendFindTaskEvent;
import com.daon.backend.notification.domain.SendFindTasksEvent;
import com.daon.backend.notification.dto.response.DesignatedManagerResponseDto;
import com.daon.backend.task.domain.board.Board;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectParticipant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Getter
@Audited(withModifiedFlag = true)
@AuditOverride(forClass = BaseEntity.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    private String title;

    private String content;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean emergency;

    @Enumerated(EnumType.STRING)
    private TaskProgressStatus progressStatus;

    @NotAudited
    // workspaceParticipantId
    private Long creatorId;

    private boolean removed;

    @Audited(withModifiedFlag = true, targetAuditMode = NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_manager_id")
    private ProjectParticipant taskManager;

    @Audited(withModifiedFlag = true, targetAuditMode = NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Audited(withModifiedFlag = true, targetAuditMode = NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @NotAudited
    @OneToMany(mappedBy = "task", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<TaskBookmark> taskBookmarks = new ArrayList<>();

    @NotAudited
    @OneToMany(mappedBy = "task", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<TaskReply> taskReplies = new ArrayList<>();

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
        if ((this.taskManager == null && taskManager != null) ||
            (this.taskManager != null && taskManager != null && taskManager.getId().equals(this.getTaskManager().getId()))) {
            publishSendAlarmEvent(taskManager);
        }

        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.emergency = emergency;
        this.progressStatus = progressStatus;
        this.board = board;
        this.taskManager = taskManager;

        publishSendTaskEvent();
        publishSendTasksEvent();
    }

    public void addTaskBookmark(TaskBookmark taskBookmark) {
        this.taskBookmarks.add(taskBookmark);
    }

    public void removeTaskBookmark(ProjectParticipant projectParticipant) {
        this.taskBookmarks.removeIf(taskBookmark -> taskBookmark.getParticipant().equals(projectParticipant));
    }

    public void modifyProgressStatus(TaskProgressStatus progressStatus) {
        this.progressStatus = Optional.ofNullable(progressStatus).orElse(this.progressStatus);

        publishSendTaskEvent();
        publishSendTasksEvent();
    }

    public void modifyTaskReplyContent(Long taskReplyId, String content) {
        TaskReply findTaskReply = this.taskReplies.stream()
                .filter(taskReply -> !taskReply.isRemoved() && taskReply.getId().equals(taskReplyId))
                .findFirst()
                .orElseThrow(() -> new TaskReplyNotFoundException(this.id, taskReplyId));

        findTaskReply.modifyTaskReplyContent(content);
    }

    public void deleteTaskReply(Long taskReplyId) {
        this.taskReplies.stream()
                .filter(taskReply -> taskReply.getId().equals(taskReplyId))
                .findFirst()
                .orElseThrow(() -> new TaskReplyNotFoundException(taskReplyId))
                .deleteTaskReply();
    }

    public void removeTaskManager() {
        this.taskManager = null;
    }

    public void removeCreator() {
        this.creatorId = null;
    }

    public void removeTask() {
        removeTaskManager();
        removeCreator();
        this.removed = true;
    }

    private void publishSendAlarmEvent(ProjectParticipant taskManager) {
        DesignatedManagerResponseDto designatedManagerEventResponse = new DesignatedManagerResponseDto(
                this.project.getWorkspace().getId(),
                this.project.getWorkspace().getTitle(),
                this.project.getId(),
                this.project.getTitle(),
                this.id,
                this.title
        );

        Events.raise(SendAlarmEvent.create(
                NotificationType.REGISTERED_TASK_MANAGER, designatedManagerEventResponse, taskManager.getMemberId()
        ));
    }

    private void publishSendTasksEvent() {
        Events.raise(SendFindTasksEvent.create(
                this.project.getWorkspace().getId(), this.project.getId(), this.board.getId()));
    }

    private void publishSendTaskEvent() {
        Events.raise(SendFindTaskEvent.create(this.getId()));
    }
}
