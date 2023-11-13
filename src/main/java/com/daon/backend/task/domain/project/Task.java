package com.daon.backend.task.domain.project;

import com.daon.backend.config.BaseTimeEntity;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private ProjectParticipant creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_manager_id")
    private ProjectParticipant taskManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "task")
    private List<TaskBookmark> taskBookmarks = new ArrayList<>();

    @Builder
    public Task(String title, String content, LocalDateTime startDate, LocalDateTime endDate, boolean emergency,
                ProjectParticipant creator, ProjectParticipant taskManager, Project project, Board board) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.emergency = emergency;
        this.creator = creator;
        this.taskManager = taskManager;
        this.project = project;
        this.board = board;

        this.progressStatus = TaskProgressStatus.TODO;
    }

    public void modifyTask(String title, String content, LocalDateTime startDate, LocalDateTime endDate, Boolean emergency,
                           TaskProgressStatus progressStatus, Board board, ProjectParticipant taskManager) {
        this.title = Optional.ofNullable(title).orElse(this.title);
        this.content = Optional.ofNullable(content).orElse(this.content);
        this.startDate = Optional.ofNullable(startDate).orElse(this.startDate);
        this.endDate = Optional.ofNullable(endDate).orElse(this.endDate);
        this.emergency = Optional.ofNullable(emergency).orElse(this.emergency);
        this.progressStatus = Optional.ofNullable(progressStatus).orElse(this.progressStatus);
        this.board = Optional.ofNullable(board).orElse(this.board);
        this.taskManager = Optional.ofNullable(taskManager).orElse(this.taskManager);
    }
}
