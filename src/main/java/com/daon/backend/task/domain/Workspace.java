package com.daon.backend.task.domain;

import com.daon.backend.config.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workspace extends BaseTimeEntity {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //DB에서 auto가 아닌 Java에서 auto
    //장단점 찾아보기 !!
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")

    @Column(name = "workspace_id", updatable = false, length = 16)
    private UUID id;

    @Column(name = "title")
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    private String description;

    private String subject;

    private String division;

    @Column(name = "join_code")
    private String joinCode;

    @Builder
    public Workspace(Long id, String name, String imageUrl, String description, String subject) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.subject = subject;
    }
}
