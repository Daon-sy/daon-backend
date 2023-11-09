package com.daon.backend.common.history;

import lombok.Getter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Entity
@RevisionEntity
public class Revision implements Serializable {

    @Id
    @GeneratedValue
    @RevisionNumber
    private Long rev;

    @RevisionTimestamp
    private Long timestamp;
}
