package com.daon.backend.member.domain;

import com.daon.backend.common.event.Events;
import com.daon.backend.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Audited
@AuditOverride(forClass = BaseEntity.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "member_id", updatable = false, length = 36)
    private String id;

    @Column(unique = true)
    private String username;

    private String password;

    private String name;

    private boolean removed;

    @NotAudited
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Email> emails = new ArrayList<>();

    @Builder
    public Member(String username, String password, String name, String email, PasswordEncoder passwordEncoder) {
        this.username = username;
        this.password = passwordEncoder.encode(password);
        this.name = name;

        this.createEmail(email);
    }

    public void checkPassword(String targetPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.match(targetPassword, this.password)) {
            throw new PasswordMismatchException(id);
        }
    }

    public void modifyMember(String prevPassword, String newPassword, String name, PasswordEncoder passwordEncoder) {
        checkPassword(prevPassword, passwordEncoder);

        this.password = Optional.ofNullable(
                StringUtils.hasText(newPassword)
                        ? passwordEncoder.encode(newPassword)
                        : null).orElse(this.password);
        this.name = Optional.ofNullable(name).orElse(this.name);
    }

    public void createEmail(String email) {
        this.emails.add(new Email(this, email));
    }

    public void removeEmail(Long memberEmailId) {
        this.emails.remove(
                this.emails.stream()
                        .filter(memberEmail -> memberEmail.getId().equals(memberEmailId))
                        .findFirst()
                        .orElseThrow(() -> new EmailNotFoundException(memberEmailId))
        );
    }

    public void withdrawMember() {
        this.removed = true;
    }

    @PostPersist
    private void raiseCreatedEvent() {
        Events.raise(MemberCreatedEvent.create(id, name, emails.get(0).getEmail()));
    }
}
