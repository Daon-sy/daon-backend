package com.daon.backend.member.domain;

import com.daon.backend.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Optional;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Audited
@AuditOverride(forClass = BaseTimeEntity.class)
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
    public Member(String email, String password, String name, PasswordEncoder passwordEncoder) {
        this.email = email;
        this.password = passwordEncoder.encode(password);
        this.name = name;
    }

    public void checkPassword(String targetPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.match(targetPassword, this.password)) {
            throw new PasswordMismatchException(id.toString());
        }
    }

    public void modifyMember(String email, String password, String name, PasswordEncoder passwordEncoder) {
        this.email = Optional.ofNullable(email).orElse(this.email);
        this.password = Optional.ofNullable(passwordEncoder.encode(password)).orElse(this.password);
        this.name = Optional.ofNullable(name).orElse(this.name);
    }
}
