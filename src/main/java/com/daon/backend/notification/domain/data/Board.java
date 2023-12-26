package com.daon.backend.notification.domain.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Column(name = "board_id")
    private Long id;

    @Column(name = "board_title")
    private String title;

    public Board(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
