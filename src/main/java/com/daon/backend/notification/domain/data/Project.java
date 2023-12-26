package com.daon.backend.notification.domain.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    @Column(name = "project_id")
    private Long id;

    @Column(name = "project_title")
    private String title;

    public Project(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
