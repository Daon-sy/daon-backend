package com.daon.backend.task.domain.task;

import com.daon.backend.config.BaseEntity;
import com.daon.backend.task.domain.project.ProjectParticipant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_reply_id")
    private Long id;

    @Column(length = 500)
    private String content;

    private boolean removed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task; //할일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false, updatable = false)
    private ProjectParticipant taskReplyWriter; //작성자

    @Builder
    public TaskReply(String content,
                     Task task,
                     ProjectParticipant taskReplyWriter) {
        this.content = content;
        this.task = task;
        this.taskReplyWriter = taskReplyWriter;
    }

    public void modifyTaskReplyContent(String content) {
        this.content = content;
    }

    public void deleteTaskReply() {
        this.removed = true;
    }
}
