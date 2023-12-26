package com.daon.backend.task.domain.workspace;

import com.daon.backend.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class WorkspaceInvitation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_invitation_id")
    private Long id;

    private String memberId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    public WorkspaceInvitation(String memberId, Workspace workspace, Role role) {
        this.memberId = memberId;
        this.workspace = workspace;
        this.role = Objects.requireNonNullElse(role, Role.BASIC_PARTICIPANT);
    }
}
