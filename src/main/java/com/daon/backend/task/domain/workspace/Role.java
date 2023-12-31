package com.daon.backend.task.domain.workspace;

import com.daon.backend.task.domain.authority.Authority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.daon.backend.task.domain.authority.Authority.*;

@Getter
@RequiredArgsConstructor
public enum Role {

    WORKSPACE_ADMIN(
            "워크스페이스 관리자",
            List.of(Authority.values())
    ),

    /**
     * 프로젝트 관리자: 워크스페이스 읽기, 프로젝트 및 할 일 전체 권한
     */
    PROJECT_ADMIN(
            "프로젝트 관리자",
            List.of(
                    WS_READ, WSN_READ,
                    MSG_CREATE, MSG_READ, MSG_DELETE,
                    PF_READ, PF_UPDATE,
                    PJ_CREATE, PJ_READ, PJ_UPDATE, PJ_DELETE, PJ_DROP,
                    BD_CREATE, BD_READ, BD_UPDATE, BD_DELETE,
                    TSK_CREATE, TSK_READ, TSK_UPDATE, TSK_DELETE,
                    REP_CREATE, REP_READ, REP_UPDATE, REP_DELETE
            )
    ),

    /**
     * 워크스페이스, 프로젝트 읽기, 할 일 전체 권한
     */
    BASIC_PARTICIPANT(
            "일반 참여자",
            List.of(
                    WS_READ, WSN_READ,
                    MSG_CREATE, MSG_READ, MSG_DELETE,
                    PF_READ, PF_UPDATE,
                    PJ_READ,
                    BD_READ,
                    TSK_CREATE, TSK_READ, TSK_UPDATE, TSK_DELETE,
                    REP_CREATE, REP_READ, REP_UPDATE, REP_DELETE
            )
    ),
    ;

    private final String roleDescription;
    private final List<Authority> authorities;
}
