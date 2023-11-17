package com.daon.backend.member.domain;

import com.daon.backend.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@Table(name = "member_email")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_email_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String email;

    public Email(Member member, String email) {
        this.member = member;
        this.email = email;
    }

    public boolean emailEquals(String email) {
        return this.email.equals(email);
    }
}
