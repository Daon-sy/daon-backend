package com.daon.backend.task.domain.project;

import com.daon.backend.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskBookmark extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_participant_id")
    private ProjectParticipant participant;

    private String memberId;

    @Builder
    public TaskBookmark(Task task, ProjectParticipant participant, String memberId) {
        this.task = task;
        this.participant = participant;
        this.memberId = memberId;
    }
}
