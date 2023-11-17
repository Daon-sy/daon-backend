package com.daon.backend.task.domain.workspace;

import com.daon.backend.config.BaseTimeEntity;
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
public class Workspace extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_id", updatable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 100)
    private String description;

    @Enumerated(EnumType.STRING)
    private Division division;

    private String imageUrl;

    @Column(length = 10)
    private String subject;

    private boolean removed;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workspace", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<WorkspaceParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "workspace", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<WorkspaceInvitation> invitations = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Workspace(String title, String description, Division division,
                      String imageUrl, String subject, WorkspaceCreator creator) {
        this.title = title;
        this.description = description;
        this.division = division;
        this.imageUrl = imageUrl;
        this.subject = subject;

        this.participants.add(
                WorkspaceParticipant.withWorkspaceAdminRole(
                        this,
                        new Profile(
                                creator.getProfileName(),
                                creator.getProfileImageUrl(),
                                creator.getProfileEmail()
                        ),
                        creator.getMemberId()
                )
        );
    }

    public static Workspace createOfPersonal(WorkspaceCreator creator) {
        String DEFAULT_TITLE_SUFFIX = "님의 개인 워크스페이스 공간";

        return Workspace.builder()
                .division(Division.PERSONAL)
                .title(creator.getProfileName() + DEFAULT_TITLE_SUFFIX)
                .creator(creator)
                .build();
    }

    public static Workspace createOfGroup(String title, String description, String imageUrl,
                                          String subject, WorkspaceCreator creator) {
        return Workspace.builder()
                .division(Division.GROUP)
                .title(title)
                .description(description)
                .imageUrl(imageUrl)
                .subject(subject)
                .creator(creator)
                .build();
    }

    public void addParticipant(String memberId, Profile profile) {
        this.participants.add(WorkspaceParticipant.withBasicParticipantRole(this, profile, memberId));
    }

    public boolean isWorkspaceParticipantsByMemberId(String memberId) {
        return this.participants.stream()
                .anyMatch(workspaceParticipant -> workspaceParticipant.getMemberId().equals(memberId));
    }

    public WorkspaceParticipant findWorkspaceParticipantByWorkspaceParticipantId(Long workspaceParticipantId, Long workspaceId) {
        return this.participants.stream()
                .filter(workspaceParticipant -> workspaceParticipant.getId().equals(workspaceParticipantId))
                .findFirst()
                .orElseThrow(() -> new NotWorkspaceParticipantException(workspaceId));
    }

    public WorkspaceParticipant findWorkspaceParticipantByMemberId(String memberId) {
        return this.participants.stream()
                .filter(workspaceParticipant -> workspaceParticipant.getMemberId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new NotWorkspaceParticipantException(memberId, id));
    }

    public void modifyWorkspace (String title, String description, String imageUrl, String subject) {
        this.title = Optional.ofNullable(title).orElse(this.title);
        this.description = Optional.ofNullable(description).orElse(this.description);
        this.imageUrl = Optional.ofNullable(imageUrl).orElse(this.imageUrl);
        this.subject = Optional.ofNullable(subject).orElse(this.subject);
    }

    public void addWorkspaceInvitation(WorkspaceInvitation workspaceInvitation) {
        this.invitations.add(workspaceInvitation);
    }

    public void removeWorkspaceInvitation(String memberId) {
        this.invitations.removeIf(workspaceInvitation -> workspaceInvitation.getMemberId().equals(memberId));
    }

    public boolean checkInvitedMember(String memberId) {
        return this.invitations.stream()
                .anyMatch(workspaceInvitation -> workspaceInvitation.getMemberId().equals(memberId));
    }

    public void withdrawWorkspace(String memberId) {
        this.participants.removeIf(workspaceParticipant -> workspaceParticipant.getMemberId().equals(memberId));
    }

    public void deportWorkspace(Long workspaceParticipantId) {
        this.participants.removeIf(
                workspaceParticipant -> workspaceParticipant.getId().equals(workspaceParticipantId)
        );
    }
}
