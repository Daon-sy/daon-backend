package com.daon.backend.task.domain.project;

import com.daon.backend.config.BaseTimeEntity;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectParticipant extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_participant_id")
    private Long id;

    private String memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_partipant_id")
    private WorkspaceParticipant workspaceParticipant;

    @Builder
    public ProjectParticipant(Project project, WorkspaceParticipant workspaceParticipant, String memberId) {
        this.project = project;
        this.workspaceParticipant = workspaceParticipant;
        this.memberId = memberId;
    }
}
