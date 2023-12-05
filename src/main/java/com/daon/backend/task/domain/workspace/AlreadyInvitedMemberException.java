package com.daon.backend.task.domain.workspace;

import com.daon.backend.common.exception.AbstractException;

public class AlreadyInvitedMemberException extends AbstractException {

    public AlreadyInvitedMemberException(String invitedMemberId) {
        super("이미 초대된 회원입니다. invitedMemberId: " + invitedMemberId);
    }
}
