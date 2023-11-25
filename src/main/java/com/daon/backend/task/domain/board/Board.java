package com.daon.backend.task.domain.board;

import com.daon.backend.config.BaseEntity;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.task.Task;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    private boolean removed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "board", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public Board(Project project, String title) {
        this.project = project;
        this.title = title;
    }

    public void modifyBoard(String title) {
        this.title = title;
    }

    public void deleteBoard() {
        this.removed = true;
    }
}
