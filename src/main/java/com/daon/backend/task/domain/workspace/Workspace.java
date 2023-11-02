package com.daon.backend.task.domain.workspace;

import com.daon.backend.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workspace extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_id")
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Division division;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Subject subject;

    private String joinCode;

    // @Transactional 끝나는 시점에 DB에 저장됌
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workspace", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<WorkspaceParticipant> participants = new ArrayList<>();

    @Builder
    public Workspace(String title, String description, Division division,
                     String imageUrl, Subject subject, String joinCode, WorkspaceCreator creator) {
        this.title = title;
        this.description = description;
        this.division = division;
        this.imageUrl = imageUrl;
        this.subject = subject;
        this.joinCode = joinCode;
        // 생성자를 관리자로 등록
        addParticipant(creator.getMemberId(), creator.getProfile());
    }

    public void addParticipant(String memberId, Profile profile) {
        this.participants.add(WorkspaceParticipant.withWorkspaceAdminRole(this, profile, memberId));
    }

}
