package com.daon.backend.task.domain.workspace;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    private String name;
    private String imageUrl;

    public Profile(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

}
