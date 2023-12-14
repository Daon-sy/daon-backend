package com.daon.backend.task.domain.project;

import com.daon.backend.common.event.Events;
import com.daon.backend.config.BaseEntity;
import com.daon.backend.task.domain.board.Board;
import com.daon.backend.task.domain.board.BoardNotFoundException;
import com.daon.backend.task.domain.board.CanNotDeleteBoardException;
import com.daon.backend.task.domain.board.SameBoardExistsException;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.daon.backend.task.domain.workspace.exception.NotWorkspaceParticipantException;
import com.daon.backend.task.dto.notification.DeportationProjectAlarmResponseDto;
import com.daon.backend.task.dto.notification.InviteProjectAlarmResponseDto;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ProjectParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @Builder
    public Project(Workspace workspace, String title, String description, ProjectCreator projectCreator) {
        String DEFAULT_BOARD_TITLE = "미분류";

        this.workspace = workspace;
        this.title = title;
        this.description = description;

        this.participants.add(new ProjectParticipant(
                this,
                projectCreator.getWorkspaceParticipant(),
                projectCreator.getMemberId())
        );
        addBoard(DEFAULT_BOARD_TITLE);
    }

    public ProjectParticipant findProjectParticipantByMemberId(String memberId) {
        return participants.stream()
                .filter(participant -> participant.getMemberId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new NotProjectParticipantException(memberId, this.id));
    }

    public ProjectParticipant findProjectParticipantByProjectParticipantId(Long projectParticipantId) {
        return participants.stream()
                .filter(participant -> participant.getId().equals(projectParticipantId))
                .findFirst()
                .orElseThrow(() -> new NotProjectParticipantException(this.id));
    }

    public void addParticipant(WorkspaceParticipant invitedWorkspaceParticipant) {
        Long invitedWorkspaceParticipantId = invitedWorkspaceParticipant.getId();
        if (participants.stream()
                .anyMatch(projectParticipant ->
                        projectParticipant.getWorkspaceParticipant().getId()
                                .equals(invitedWorkspaceParticipantId))) {
            throw new AlreadyInvitedWorkspaceParticipantException(invitedWorkspaceParticipantId);
        }

        String invitedWorkspaceParticipantMemberId = invitedWorkspaceParticipant.getMemberId();
        this.participants.add(
                new ProjectParticipant(this, invitedWorkspaceParticipant, invitedWorkspaceParticipantMemberId)
        );

        Events.raise(new InviteProjectAlarmEvent(
                new InviteProjectAlarmResponseDto(
                        invitedWorkspaceParticipant.getWorkspace().getId(),
                        invitedWorkspaceParticipant.getWorkspace().getTitle(),
                        this.id,
                        this.title
                ),
                invitedWorkspaceParticipantMemberId)
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
        boolean isRemoved = this.participants.removeIf(projectParticipant ->
                projectParticipant.getMemberId().equals(memberId)
        );
        if (!isRemoved) {
            throw new NotProjectParticipantException(memberId, this.id);
        }
    }

    public void deportProject(Long projectParticipantId) {
        ProjectParticipant projectParticipant = this.participants.stream()
                .filter(participant -> participant.getId().equals(projectParticipantId))
                .findFirst()
                .orElseThrow(() -> new NotProjectParticipantException(this.id));
        this.participants.remove(projectParticipant);

        Events.raise(new DeportationProjectAlarmEvent(
                new DeportationProjectAlarmResponseDto(
                        this.workspace.getId(),
                        this.workspace.getTitle(),
                        this.id,
                        this.title
                ), projectParticipant.getMemberId()
        ));
    }

    public void deleteBoard(Long boardId) {
        if (this.getBoards().size() > 1) {
            boolean isRemoved = this.boards.removeIf(board -> board.getId().equals(boardId));
            if (!isRemoved) {
                throw new BoardNotFoundException(this.id, boardId);
            }
        } else {
            throw new CanNotDeleteBoardException();
        }
    }
}
