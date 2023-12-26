package com.daon.backend.notification.domain.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workspace {

    @Column(name = "workspace_id")
    private Long id;

    @Column(name = "workspace_title")
    private String title;

    public Workspace(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
