package com.daon.backend.task.domain.project;

import com.daon.backend.common.event.Events;
import com.daon.backend.config.BaseEntity;
import com.daon.backend.notification.domain.NotificationType;
import com.daon.backend.notification.domain.SendAlarmEvent;
import com.daon.backend.notification.dto.response.DeportationProjectResponseDto;
import com.daon.backend.notification.dto.response.InviteProjectAlarmResponseDto;
import com.daon.backend.task.domain.board.Board;
import com.daon.backend.task.domain.board.BoardNotFoundException;
import com.daon.backend.task.domain.board.SameBoardExistsException;
import com.daon.backend.task.domain.task.Task;
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
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    private String title;

    private String description;

    private boolean removed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ProjectParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Task> tasks = new ArrayList<>();

    @Builder
    public Project(Workspace workspace, String title, String description, ProjectCreator projectCreator) {
        String DEFAULT_BOARD_TITLE = "미분류";

        this.workspace = workspace;
        this.title = title;
        this.description = description;

        addParticipant(projectCreator.getMemberId(), projectCreator.getWorkspaceParticipant());
        addBoard(DEFAULT_BOARD_TITLE);
    }

    public Optional<ProjectParticipant> findProjectParticipantByMemberId(String memberId) {
        return participants.stream().filter(participant -> participant.getMemberId().equals(memberId)).findFirst();
    }

    public Optional<ProjectParticipant> findProjectParticipantByProjectParticipantId(Long projectParticipantId) {
        return participants.stream().filter(participant -> participant.getId().equals(projectParticipantId)).findFirst();
    }

    public void addParticipant(String memberId, WorkspaceParticipant workspaceParticipant) {
        this.participants.add(new ProjectParticipant(this, workspaceParticipant, memberId));

        InviteProjectAlarmResponseDto inviteEventResponse = createInviteEventResponse(workspaceParticipant);
        Events.raise(SendAlarmEvent.create(NotificationType.INVITE_PROJECT, inviteEventResponse, memberId));
    }

    private InviteProjectAlarmResponseDto createInviteEventResponse(WorkspaceParticipant workspaceParticipant) {
        return new InviteProjectAlarmResponseDto(
                workspaceParticipant.getWorkspace().getId(), workspaceParticipant.getWorkspace().getTitle(),
                this.id, this.title
        );
    }

    public void addBoard(String title) {
        this.boards.add(new Board(this, title));
    }

    public Board getBoardByBoardId(Long boardId) {
        if (boardId == null) {
            return null;
        }
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

    public boolean isProjectParticipants(String memberId) {
        return this.participants.stream()
                .anyMatch(projectParticipant -> projectParticipant.getMemberId().equals(memberId));
    }

    public void modifyProject(String title, String description) {
        this.title = Optional.ofNullable(title).orElse(this.title);
        this.description = Optional.ofNullable(description).orElse(this.description);
    }

    public void withdrawProject(String memberId) {
        this.participants.removeIf(
                projectParticipant -> projectParticipant.getMemberId().equals(memberId)
        );
    }

    public void deportProject(Long projectParticipantId) {
        ProjectParticipant projectParticipant = this.participants.stream()
                .filter(participant -> participant.getId().equals(projectParticipantId))
                .findFirst()
                .orElseThrow(() -> new NotProjectParticipantException(this.id));
        this.participants.remove(projectParticipant);

        DeportationProjectResponseDto deportationEventResponse = createDeportationEventResponse();
        Events.raise(SendAlarmEvent.create(
                NotificationType.DEPORTATION_PROJECT, deportationEventResponse, projectParticipant.getMemberId())
        );
    }

    private DeportationProjectResponseDto createDeportationEventResponse() {
        return new DeportationProjectResponseDto(
                this.workspace.getId(), this.workspace.getTitle(), this.id, this.title);
    }

    public void removeProject() {
        this.participants.clear();
        this.removed = true;
    }
}
