package com.daon.backend.task.domain.task;

import com.daon.backend.config.BaseTimeEntity;
import com.daon.backend.task.domain.project.ProjectParticipant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskReply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskReply_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task; //할일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false, updatable = false)
    private ProjectParticipant taskReplyWriter; //작성자

    private boolean removed;

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
}
