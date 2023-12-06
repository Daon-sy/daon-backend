package com.daon.backend.task.domain.workspace;

import com.daon.backend.common.event.Events;
import com.daon.backend.config.BaseEntity;
import com.daon.backend.task.dto.notification.SendReceiveMessageAlarmResponseDto;
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

    @Transient
    private WorkspaceParticipant sender;

    @Transient
    private WorkspaceParticipant receiver;

    @Builder
    public Message(String title,
                   String content,
                   Long receiverId,
                   Long senderId,
                   Workspace workspace,
                   WorkspaceParticipant sender,
                   WorkspaceParticipant receiver) {
        this.title = title;
        this.content = content;
        this.readed = false;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.workspace = workspace;
        this.sender = sender;
        this.receiver = receiver;
    }

    public void readMessage() {
        this.readed = true;
    }

    @PostPersist
    private void createMessageEvent() {
        Events.raise(new SendReceiveMessageAlarmEvent(
                new SendReceiveMessageAlarmResponseDto(
                        workspace.getId(),
                        workspace.getTitle(),
                        sender,
                        id,
                        getCreatedAt()
                ),
                receiver.getMemberId()
        ));
    }
}
