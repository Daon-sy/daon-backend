package com.daon.backend.task.domain.workspace;

import com.daon.backend.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkspaceNotice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id", updatable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false,length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @Audited(withModifiedFlag = true, targetAuditMode = NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_partipant_id")
    private WorkspaceParticipant workspaceNoticeWriter;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkspaceNotice> workspaceNotices = new ArrayList<>();

    @Builder
    private WorkspaceNotice(Workspace workspace, WorkspaceParticipant workspaceNoticeWriter, String title, String content ){
        this.workspace = workspace;
        this.workspaceNoticeWriter = workspaceNoticeWriter;
        this.title = title;
        this.content = content;
    }

    public void modifyWorkspaceNotice(WorkspaceParticipant newWorkspaceNoticeWriter, String title, String content){
        if (!Objects.equals(this.workspaceNoticeWriter, newWorkspaceNoticeWriter)) {
            this.workspaceNoticeWriter = newWorkspaceNoticeWriter;
        }
        this.title = Optional.ofNullable(title).orElse(this.title);
        this.content=Optional.ofNullable(content).orElse(this.content);
    }

    public void deleteWorkspaceNotice(Long noticeId){
        this.workspaceNotices.remove(
                this.workspaceNotices.stream()
                        .filter(notice -> notice.getId().equals(noticeId))
                        .findFirst()
                        .orElseThrow(()-> new WorkspaceNoticeNotFoundException(noticeId))
        );
    }
}
