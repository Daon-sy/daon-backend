package com.daon.backend.task.domain;

import com.daon.backend.config.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "workspace_participants_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //DB에서 auto가 아닌 Java에서 auto
    //장단점 찾아보기 !!
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")

    @Column(name = "workspace_participations_id", updatable = false, length = 16)
    private UUID id;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "workspace_id")
    private String workspaceId;

    private String nickname;

    @Column(name = "image_url")
    private String imageUrl;

    @Builder
    public Profile(Long id, String nickname, String imageUrl) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}
