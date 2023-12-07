package com.daon.backend.task.domain.workspace;

import com.daon.backend.common.exception.AbstractException;

public class WorkspaceNoticeNotFoundException extends AbstractException {

    public WorkspaceNoticeNotFoundException(Long workspaceNoticeId){
        super("존재하지 않는 워크스페이스 공지사항입니다. 요청된 워크스페이스 공지사항 식별값: " + workspaceNoticeId);
    }
}
