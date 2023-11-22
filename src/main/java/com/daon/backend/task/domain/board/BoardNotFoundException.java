package com.daon.backend.task.domain.board;

import com.daon.backend.common.exception.AbstractException;

public class BoardNotFoundException extends AbstractException {

    public BoardNotFoundException(Long projectId, Long boardId) {
        super("해당 프로젝트에 유효한 보드가 없습니다. projectId: " + projectId + ", requestedBoardId: " + boardId);
    }
}
