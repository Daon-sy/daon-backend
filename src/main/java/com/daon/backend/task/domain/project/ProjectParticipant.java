package com.daon.backend.task.domain.project;

import com.daon.backend.config.BaseEntity;
import com.daon.backend.task.domain.task.TaskReply;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectParticipant extends BaseEntity {

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

    @OneToMany(mappedBy = "taskReplyWriter", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<TaskReply> taskReplies = new ArrayList<>();

    @Builder
    public ProjectParticipant(Project project, WorkspaceParticipant workspaceParticipant, String memberId) {
        this.project = project;
        this.workspaceParticipant = workspaceParticipant;
        this.memberId = memberId;
    }
}
