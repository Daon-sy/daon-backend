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
    private String email;

    public Profile(String name, String imageUrl, String email) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.email = email;
    }

}
