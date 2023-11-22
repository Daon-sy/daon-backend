package com.daon.backend.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통
    INTERNAL_SERVER_ERROR(1000, "서버 내부 오류입니다. 오류 문의 부탁드립니다."),
    METHOD_ARGUMENT_NOT_VALID(1001, "요청 데이터 검증에 실패했습니다. API 스펙을 확인해 주세요."),

    MEMBER_NOT_AUTHENTICATED(1500, "로그인되지 않은 유저입니다. 로그인 후 이용해주세요."),
    INVALID_ACCESS_TOKEN(1501, "액세스 토큰이 유효하지 않습니다. 액세스 토큰 재발급을 시도해 주세요."),
    INVALID_REFRESH_TOKEN(1502, "리프레시 토큰이 유효하지 않습니다. 다시 로그인 해주세요."),
    UNAUTHORIZED_MEMBER(1503, "해당 요청에 대한 권한이 없습니다."),

    TYPE_NOT_SPECIFIC(1700, "SSE 연결 요청(할 일 목록 조회)에 타입이 지정되지 않았습니다. Param을 확인해 주세요."),

    INVALID_TARGET(1800, "유효하지 않은 분류 값입니다. 검색 필터를 확인해 주세요."),

    IMAGE_IOEXCEPTION(1900, "이미지 업로드 중 오류가 발생했습니다."),
    NOT_ALLOWED_CONTENT_TYPE(1901, "이미지는 .jpg, .jpeg, .png 형식이어야 합니다."),
    EMPTY_IMAGE(1902, "빈 이미지 파일입니다."),

    // 회원
    MEMBER_NOT_FOUND(2000, "존재하지 않는 회원 아이디입니다."),
    ALREADY_EXISTS_MEMBER(2001, "이미 존재하는 회원입니다."),
    PASSWORD_MISMATCH(2002, "비밀번호가 일치하지 않습니다."),
    EMAIL_NOT_FOUND(2003, "존재하지 않는 이메일입니다."),

    // 워크스페이스
    WORKSPACE_NOT_FOUND(3000, "요청한 워크스페이스는 존재하지 않습니다."),
    NOT_WORKSPACE_PARTICIPANT(3001, "해당 워크스페이스에 접근 권한이 없습니다."),
    SAME_MEMBER_EXISTS_IN_WORKSPACE(3002, "해당 워크스페이스에 이미 참여 중인 회원입니다."),
    NOT_INVITED_MEMBER(3003, "해당 워크스페이스에 초대 받은 회원이 아닙니다."),
    CAN_NOT_DELETE_PERSONAL_WORKSPACE(3004, "개인 워크스페이스는 삭제할 수 없습니다."),

    // 프로젝트
    PROJECT_NOT_FOUND(4000, "존재하지 않는 프로젝트입니다."),
    NOT_PROJECT_PARTICIPANT(4001, "해당 프로젝트의 회원이 아닙니다."),

    BOARD_NOT_FOUND(4100, "사용 중인 보드가 없습니다."),
    SAME_BOARD_EXISTS(4101, "동일한 이름의 보드가 존재합니다."),
    CAN_NOT_DELETE_BOARD(4102, "보드가 1개 남았기 때문에 삭제할 수 없습니다."),

    // 할 일
    TASK_NOT_FOUND(5000, "생성된 할 일이 없습니다."),
    ;

    private final int code;

    private final String description;

}
