package com.daon.backend.task.domain.workspace;

import com.daon.backend.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkspaceParticipant extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_participant_id")
    private Long id;

    @Embedded
    private Profile profile;

    private String memberId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    private WorkspaceParticipant(Workspace workspace, Profile profile, String memberId, Role role) {
        this.workspace = workspace;
        this.profile = profile;
        this.memberId = memberId;
        this.role = role;
    }

    public static WorkspaceParticipant withWorkspaceAdminRole(Workspace workspace, Profile profile, String memberId) {
        return new WorkspaceParticipant(workspace, profile, memberId, Role.WORKSPACE_ADMIN);
    }

    public static WorkspaceParticipant withRole(Workspace workspace,
                                                Profile profile,
                                                String memberId,
                                                Role role) {
        return new WorkspaceParticipant(workspace, profile, memberId, role);
    }

    public void modifyRole(Role role) {
        this.role = Optional.ofNullable(role).orElse(this.role);
    }

    public boolean memberIdEquals(String memberId) {
        return this.memberId.equals(memberId);
    }

    public boolean isWorkspaceAdmin() {
        return role.equals(Role.WORKSPACE_ADMIN);
    }
}
