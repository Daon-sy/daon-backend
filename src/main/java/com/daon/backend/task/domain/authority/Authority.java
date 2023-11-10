package com.daon.backend.task.domain.authority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Authority {

    // workspace
    WS_READ("워크스페이스 조회"),
    WS_UPDATE("워크스페이스 수정"),
    WS_DELETE("워크스페이스 삭제"),
    // workspace-participant
    WSP_INVITE("워크스페이스 사용자 초대"),
    WSP_DROP("워크스페이스 사용자 내보내기"),
    WSP_ROLE_UPDATE("워크스페이스 사용자 역할 변경"),
    // project
    PJ_CREATE("프로젝트 생성"),
    PJ_READ("프로젝트 조회"),
    PJ_UPDATE("프로젝트 수정"),
    PJ_DELETE("프로젝트 삭제"),
    //board
    BD_CREATE("보드 생성"),
    BD_READ("보드 조회"),
    BD_UPDATE("보드 수정"),
    BD_DELETE("보드 삭제"),
    // task
    TSK_CREATE("할 일 생성"),
    TSK_READ("할 일 읽기"),
    TSK_UPDATE("할 일 수정"),
    TSK_DELETE("할 일 삭제"),
    ;

    private final String description;

}
