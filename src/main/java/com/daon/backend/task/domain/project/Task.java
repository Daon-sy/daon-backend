package com.daon.backend.task.domain.project;

import com.daon.backend.config.BaseTimeEntity;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private ProjectParticipant creator; //생성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_partipant_id")
    private WorkspaceParticipant taskManager; //담당자?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "task")
    private List<TaskBookmark> taskBookmarks = new ArrayList<>();

    @Builder
    public Task(String title,
                String content,
                LocalDateTime startDate,
                LocalDateTime endDate,
                boolean emergency,
                TaskProgressStatus progressStatus,
                ProjectParticipant creator,
                WorkspaceParticipant taskManager,
                Project project,
                Board board) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.emergency = emergency;
        this.progressStatus = progressStatus;
        this.creator = creator;
        this.taskManager = taskManager;
        this.project = project;
        this.board = board;
    }
}
