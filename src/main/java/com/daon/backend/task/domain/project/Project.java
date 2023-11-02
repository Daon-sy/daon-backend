package com.daon.backend.task.domain.project;

import com.daon.backend.config.BaseTimeEntity;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    private String title;
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ProjectParticipant> participants = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Board> boards = new ArrayList<>();

    public Project(Workspace workspace, String title, String description) {
        this.workspace = workspace;
        this.title = title;
        this.description = description;
    }

    public void addParticipant(String memberId, WorkspaceParticipant workspaceParticipant) {
        this.participants.add(new ProjectParticipant(this, workspaceParticipant, memberId));
    }

    public void addBoard(String title) {
        this.boards.add(new Board(this, title));
    }

}
