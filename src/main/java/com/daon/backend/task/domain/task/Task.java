package com.daon.backend.task.domain.task;

import com.daon.backend.common.event.Events;
import com.daon.backend.config.BaseEntity;
import com.daon.backend.task.domain.board.Board;
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

    @Audited(withModifiedFlag = true, targetAuditMode = NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_manager_id")
    private ProjectParticipant taskManager;

    @Audited(withModifiedFlag = true, targetAuditMode = NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @NotAudited
    @OneToMany(mappedBy = "task", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<TaskReply> taskReplies = new ArrayList<>();

    @Builder
    public Task(String title, String content, LocalDateTime startDate, LocalDateTime endDate, boolean emergency,
                Long creatorId, ProjectParticipant taskManager, Board board) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.emergency = emergency;
        this.creatorId = creatorId;
        this.board = board;

        assignTo(taskManager);

        this.progressStatus = TaskProgressStatus.TODO;
    }

    public void assignTo(ProjectParticipant taskManager) {
        if (taskManager == null) {
            this.taskManager = null;
            return;
        }

        if (this.taskManager == null) {
            this.taskManager = taskManager;
            Events.raise(TaskEvent.assigned(this));
            return;
        }

        if (!this.taskManager.getId().equals(taskManager.getId())) {
            this.taskManager = taskManager;
            Events.raise(TaskEvent.assigned(this));
        }
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

        assignTo(taskManager);
    }

    public void modifyProgressStatus(TaskProgressStatus progressStatus) {
        this.progressStatus = Optional.ofNullable(progressStatus).orElse(this.progressStatus);
    }

    public void modifyTaskReplyContent(Long taskReplyId, String content) {
        TaskReply findTaskReply = this.taskReplies.stream()
                .filter(taskReply -> taskReply.getId().equals(taskReplyId))
                .findFirst()
                .orElseThrow(() -> new TaskReplyNotFoundException(this.id, taskReplyId));

        findTaskReply.modifyTaskReplyContent(content);
    }

    public void deleteTaskReply(Long taskReplyId) {
        this.taskReplies.remove(
                this.taskReplies.stream()
                        .filter(taskReply -> taskReply.getId().equals(taskReplyId))
                        .findFirst()
                        .orElseThrow(() -> new TaskReplyNotFoundException(taskReplyId))
        );
    }

    @PostPersist
    private void raiseTaskCreatedEvent() {
        Events.raise(TaskEvent.created(this));
    }

    @PostUpdate
    private void raiseTaskModifiedEvent() {
        Events.raise(TaskEvent.modified(this));
    }

    @PostRemove
    private void raiseTaskRemovedEvent() {
        Events.raise(TaskEvent.removed(this));
    }
}
