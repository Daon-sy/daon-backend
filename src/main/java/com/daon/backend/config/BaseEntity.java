package com.daon.backend.config;

import lombok.Getter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @NotAudited
    @CreatedDate
    private LocalDateTime createdAt;

    @Audited
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @NotAudited
    @CreatedBy
    private String createdBy;

    @Audited
    @LastModifiedBy
    private String modifiedBy;
}
