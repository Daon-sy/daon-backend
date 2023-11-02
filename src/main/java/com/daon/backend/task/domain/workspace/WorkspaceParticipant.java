package com.daon.backend.task.domain.workspace;

import com.daon.backend.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @Builder

    public WorkspaceParticipant(Workspace workspace, Profile profile, String memberId) {
        this.workspace = workspace;
        this.profile = profile;
        this.memberId = memberId;
    }
}
