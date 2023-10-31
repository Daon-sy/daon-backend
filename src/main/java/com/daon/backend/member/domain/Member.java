package com.daon.backend.member.domain;

import com.daon.backend.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "member_id", updatable = false, length = 16)
    private UUID id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Builder
    public Member(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void checkPassword(String password) {
        if (!this.password.equals(password)) {
            throw new IllegalArgumentException("password does not match");
        }
    }
}
