package com.daon.backend.task.domain.task;

import com.daon.backend.config.BaseTimeEntity;
import com.daon.backend.task.domain.project.ProjectParticipant;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task; //할일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false, updatable = false)
    private ProjectParticipant writer; //작성자

    private boolean removed;

    @Builder
    public Reply(String content,
                 Task task,
                 ProjectParticipant writer) {
        this.content = content;
        this.task = task;
        this.writer = writer;
    }

    public void modifyContent(String content) {
        this.content = content;
    }

    public void deleteReply() {
        this.removed = true;
    }
}
