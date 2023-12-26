package com.daon.backend.member.domain;

import com.daon.backend.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@Table(name = "member_email")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email extends BaseEntity {

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
}
