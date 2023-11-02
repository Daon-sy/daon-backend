package com.daon.backend.task.domain.project;

import com.daon.backend.config.BaseTimeEntity;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectParticipant extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_partipant_id")
    private WorkspaceParticipant workspaceParticipant;

    // 회원 기본키
    private String memberId;

    public ProjectParticipant(Project project, WorkspaceParticipant workspaceParticipant, String memberId) {
        this.project = project;
        this.workspaceParticipant = workspaceParticipant;
        this.memberId = memberId;
    }
}
