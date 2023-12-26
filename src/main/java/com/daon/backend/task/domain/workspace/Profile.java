package com.daon.backend.task.domain.workspace;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Optional;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Column(nullable = false, length = 20)
    private String name;

    private String imageUrl;

    private String email;

    public Profile(String name, String imageUrl, String email) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.email = email;
    }

    public void modifyProfile(String name, String imageUrl, String email) {
        this.name = Optional.ofNullable(name).orElse(this.name);
        this.imageUrl = Optional.ofNullable(imageUrl).orElse(this.imageUrl);
        this.email = Optional.ofNullable(email).orElse(this.email);
    }
}
