package com.daon.backend.task.domain.project;

import com.daon.backend.config.BaseTimeEntity;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks = new ArrayList<>();

    private String title;
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ProjectParticipant> participants = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @Builder
    public Project(Workspace workspace, String title, String description, ProjectCreator projectCreator) {
        this.workspace = workspace;
        this.title = title;
        this.description = description;

        addParticipant(projectCreator.getMemberId(), projectCreator.getWorkspaceParticipant());
    }

    public Optional<ProjectParticipant> findProjectParticipantByMemberId(String memberId) {
        return participants.stream().filter(participant -> participant.getMemberId().equals(memberId)).findFirst();
    }

    public Optional<ProjectParticipant> findProjectParticipantByProjectParticipantId(Long projectParticipantId) {
        return participants.stream().filter(participant -> participant.getId().equals(projectParticipantId)).findFirst();
    }

    public void addParticipant(String memberId, WorkspaceParticipant workspaceParticipant) {
        this.participants.add(new ProjectParticipant(this, workspaceParticipant, memberId));
    }

    public void addBoard(String title) {
        this.boards.add(new Board(this, title));
    }

    public void removeBoard(Long boardId) {
        this.boards.removeIf(board -> board.getId().equals(boardId));
    }

    public Board getBoardByBoardId(Long boardId) {
        if (boardId == null) return null;
        return boards.stream()
                .filter(board -> board.getId().equals(boardId))
                .findFirst()
                .orElseThrow(() -> new BoardNotFoundException(this.getId(), boardId));
    }

    public void throwIfTitleExist(String title) {
        boards.stream()
                .filter(board -> board.getTitle().equals(title))
                .findFirst().ifPresent(board -> {
                    throw new SameBoardExistsException(title);
                });
    }
}
