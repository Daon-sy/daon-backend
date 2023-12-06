package com.daon.backend.task.domain.workspace;

import com.daon.backend.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", updatable = false)
    private Long id;

    private String title;

    private String content;

    private boolean readed;

    // workspaceParticipantId
    private Long receiverId;

    // workspaceParticipantId
    private Long senderId;

    @Audited(withModifiedFlag = true, targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @Builder
    public Message(String title, String content, Long receiverId, Long senderId, Workspace workspace) {
        this.title = title;
        this.content = content;
        this.readed = false;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.workspace = workspace;
    }

    public void readMessage() {
        this.readed = true;
    }
}
