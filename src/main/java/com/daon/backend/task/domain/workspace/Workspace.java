package com.daon.backend.task.domain.workspace;

import com.daon.backend.common.event.Events;
import com.daon.backend.config.BaseEntity;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectNotFoundException;
import com.daon.backend.task.domain.workspace.exception.*;
import com.daon.backend.task.dto.notification.DeportationWorkspaceAlarmResponseDto;
import com.daon.backend.task.dto.notification.InviteWorkspaceAlarmResponseDto;
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
public class Workspace extends BaseEntity {

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

    @OneToMany(mappedBy = "workspace", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<WorkspaceParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "workspace", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<WorkspaceInvitation> invitations = new ArrayList<>();

    @OneToMany(mappedBy = "workspace", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "workspace", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<WorkspaceNotice> workspaceNotices = new ArrayList<>();

    @OneToMany(mappedBy = "workspace", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

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
        WorkspaceInvitation invitation = this.invitations.stream()
                .filter(workspaceInvitation -> workspaceInvitation.getMemberId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new NotInvitedMemberException(this.id, memberId));

        this.participants.add(
                WorkspaceParticipant.withRole(this, profile, memberId, invitation.getRole())
        );

        this.invitations.removeIf(workspaceInvitation -> workspaceInvitation.getMemberId().equals(memberId));
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

    public Project findProject(Long projectId) {
        return this.projects.stream()
                .filter(project -> project.getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    public void modifyWorkspace(String title, String description, String imageUrl, String subject) {
        this.title = Optional.ofNullable(title).orElse(this.title);
        this.description = Optional.ofNullable(description).orElse(this.description);
        this.imageUrl = Optional.ofNullable(imageUrl).orElse(this.imageUrl);
        this.subject = Optional.ofNullable(subject).orElse(this.subject);
    }

    public void addWorkspaceInvitation(String invitedMemberId, WorkspaceInvitation workspaceInvitation) {
        if (!isPersonal()) {
            boolean alreadyInvited = this.invitations.stream()
                    .anyMatch(invitation -> invitation.getMemberId().equals(invitedMemberId));
            if (alreadyInvited) {
                throw new AlreadyInvitedMemberException(invitedMemberId);
            } else {
                this.invitations.add(workspaceInvitation);

                Events.raise(new InviteWorkspaceAlarmEvent(
                        new InviteWorkspaceAlarmResponseDto(this.id, this.title),
                        workspaceInvitation.getMemberId()
                ));
            }
        } else {
            throw new CanNotInvitePersonalWorkspaceException(this.id);
        }
    }

    public void removeWorkspaceInvitation(String memberId) {
        this.invitations.removeIf(workspaceInvitation -> workspaceInvitation.getMemberId().equals(memberId));
    }

    public WorkspaceNotice findWorkspaceNoticeById(Long noticeId) {
        return this.workspaceNotices.stream()
                .filter(notice -> notice.getId().equals(noticeId))
                .findFirst()
                .orElseThrow(() -> new WorkspaceNoticeNotFoundException(noticeId));
    }

    public void removeWorkspaceNotice(Long noticeId) {
        WorkspaceNotice notice = findWorkspaceNoticeById(noticeId);
        this.workspaceNotices.remove(notice);
    }

    public boolean checkInvitedMember(String memberId) {
        return this.invitations.stream()
                .anyMatch(workspaceInvitation -> workspaceInvitation.getMemberId().equals(memberId));
    }

    public WorkspaceParticipant getWorkspaceParticipant(Long workspaceParticipantId) {
        return this.participants.stream()
                .filter(workspaceParticipant -> workspaceParticipant.getId().equals(workspaceParticipantId))
                .findFirst()
                .orElseThrow(() -> new NotWorkspaceParticipantException(this.id));
    }

    public List<WorkspaceParticipant> getWorkspaceParticipants() {
        return this.participants;
    }

    public boolean isPersonal() {
        return this.division.equals(Division.PERSONAL);
    }

    public boolean canWithdrawWorkspace() {
        return this.participants.stream()
                .filter(workspaceParticipant -> workspaceParticipant.getRole().equals(Role.WORKSPACE_ADMIN))
                .count() > 1;
    }

    public void withdrawWorkspace(String memberId) {
        this.participants.removeIf(workspaceParticipant -> workspaceParticipant.getMemberId().equals(memberId));
    }

    public void deportWorkspace(Long workspaceParticipantId, String workspaceParticipantMemberId) {
        this.participants.removeIf(
                workspaceParticipant -> workspaceParticipant.getId().equals(workspaceParticipantId)
        );

        Events.raise(new DeportationWorkspaceAlarmEvent(
                new DeportationWorkspaceAlarmResponseDto(this.id, this.title),
                workspaceParticipantMemberId
        ));
    }

    public void deleteWorkspace() {
        this.participants.clear();
        this.invitations.clear();
        this.removed = true;
    }

    public void resetWorkspace(String memberId) {
        if (isPersonal()) {
            Profile profile = this.participants.get(0).getProfile();

            this.participants.clear();
            this.projects.clear();
            this.title = profile.getName() + "님의 개인 워크스페이스 공간";
            this.description = null;
            this.subject = null;
            this.imageUrl = null;

            this.participants.add(WorkspaceParticipant.withWorkspaceAdminRole(this, profile, memberId));
        } else {
            throw new CanNotDeletePersonalWorkspaceException(this.id);
        }
    }

    public void saveMessage(String title, String content, WorkspaceParticipant receiver, WorkspaceParticipant sender) {
        this.messages.add(
                new Message(title, content, receiver.getId(), sender.getId(), this, sender, receiver)
        );
    }

    public void checkMessageCanBeSend(Long senderId, Long receiverId) {
        if (this.participants.stream()
                .noneMatch(workspaceParticipant -> workspaceParticipant.getId().equals(receiverId))) {
            throw new NotWorkspaceParticipantException(this.id);
        }
        if (senderId.equals(receiverId)) {
            throw new CanNotSendMessageToMeException(senderId);
        }
    }

    public Message findMessage(Long messageId, Long receiverId) {
        Message findMessage = this.messages.stream()
                .filter(message -> message.getId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new MessageNotFoundException(messageId));

        if (!findMessage.getReceiverId().equals(receiverId)) {
            throw new NotTheMessageReceiverException();
        }

        return findMessage;
    }

    public void deleteMessage(Long messageId, Long receiverId) {
        Message findMessage = findMessage(messageId, receiverId);
        this.messages.removeIf(message -> message.equals(findMessage));
    }
}
