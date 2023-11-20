package com.daon.backend.task.domain.project;

import com.daon.backend.common.exception.AbstractException;

public class NotProjectParticipantException extends AbstractException {

    public NotProjectParticipantException(String memberId, Long projectId) {
        super("해당 프로젝트의 회원이 아닙니다. memberId: " + memberId + ", projectId: " + projectId);
    }

    public NotProjectParticipantException(Long projectId) {
        super("해당 프로젝트의 회원이 아닙니다. projectId: " + projectId);
    }
}
