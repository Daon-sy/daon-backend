package com.daon.backend.member.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Settings {

    @Column(nullable = false)
    private boolean notified;

    public Settings() {
        this.notified = true;
    }

    public Settings(boolean notified) {
        this.notified = notified;
    }
}
