package com.daon.backend.task.domain.workspace;

import com.daon.backend.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkspaceParticipant extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @Embedded
    private Profile profile;

    // 회원 기본키
    private String memberId;

    // TODO 역할 필요
    @Enumerated(EnumType.STRING)
    private Role role;

    private WorkspaceParticipant(Workspace workspace, Profile profile, String memberId, Role role) {
        this.workspace = workspace;
        this.profile = profile;
        this.memberId = memberId;
        this.role = role;
    }

    public static WorkspaceParticipant withWorkspaceAdminRole(Workspace workspace, Profile profile, String memberId) {
        return new WorkspaceParticipant(workspace, profile, memberId, Role.WORKSPACE_ADMIN);
    }

    public static WorkspaceParticipant withProjectAdminRole(Workspace workspace, Profile profile, String memberId) {
        return new WorkspaceParticipant(workspace, profile, memberId, Role.PROJECT_ADMIN);
    }

    public static WorkspaceParticipant withBasicParticipantRole(Workspace workspace, Profile profile, String memberId) {
        return new WorkspaceParticipant(workspace, profile, memberId, Role.BASIC_PARTICIPANT);
    }

    public void modifyRole(Role role) {
        this.role = Optional.ofNullable(role).orElse(this.role);
    }
}
