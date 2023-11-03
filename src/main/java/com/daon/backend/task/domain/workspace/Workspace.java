package com.daon.backend.task.domain.workspace;

import com.daon.backend.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workspace extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_id")
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Division division;

    private String imageUrl;

    private String subject;

    private String joinCode;

    // @Transactional 끝나는 시점에 DB에 저장됌
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workspace", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<WorkspaceParticipant> participants = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Workspace(String title, String description, Division division,
                      String imageUrl, String subject, WorkspaceCreator creator) {
        this.title = title;
        this.description = description;
        this.division = division;
        this.imageUrl = imageUrl;
        this.subject = subject;
        this.joinCode = generateJoinCode();

        // 생성자를 관리자로 등록
        this.participants.add(
                WorkspaceParticipant.withWorkspaceAdminRole(
                        this,
                        new Profile(creator.getProfileName(), creator.getProfileImageUrl()),
                        creator.getMemberId()
                )
        );
    }

    public static Workspace createOfPersonal(String title, String description, String imageUrl,
                                             String subject, WorkspaceCreator creator) {
        return Workspace.builder()
                .division(Division.PERSONAL)
                .title(title)
                .description(description)
                .imageUrl(imageUrl)
                .subject(subject)
                .creator(creator)
                .build();
    }

    public static Workspace createOfGroup(String title, String description, String imageUrl,
                                          String subject, WorkspaceCreator creator) {
        return Workspace.builder()
                .division(Division.GROUP)
                .title(title)
                .description(description)
                .imageUrl(imageUrl)
                .subject(subject)
                .creator(creator)
                .build();
    }

    public void addParticipant(String memberId, Profile profile) {
        this.participants.add(WorkspaceParticipant.withBasicParticipantRole(this, profile, memberId));
    }

    private String generateJoinCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

}
