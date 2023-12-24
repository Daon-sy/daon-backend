package com.daon.backend.task.domain.project;

import com.daon.backend.config.BaseEntity;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskBookmark;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectParticipant extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_participant_id")
    private Long id;

    private String memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_partipant_id")
    private WorkspaceParticipant workspaceParticipant;

    @OneToMany(mappedBy = "participant", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<TaskBookmark> taskBookmarks = new ArrayList<>();

    @Builder
    public ProjectParticipant(Project project, WorkspaceParticipant workspaceParticipant, String memberId) {
        this.project = project;
        this.workspaceParticipant = workspaceParticipant;
        this.memberId = memberId;
    }

    public void addTaskBookmark(Task task) {
        this.taskBookmarks.add(new TaskBookmark(task, this, memberId));
    }

    public void removeTaskBookmark(Task task) {
        this.taskBookmarks.removeIf(taskBookmark -> taskBookmark.getTask().getId().equals(task.getId()));
    }
}
