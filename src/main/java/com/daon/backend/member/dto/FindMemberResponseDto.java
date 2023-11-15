package com.daon.backend.member.dto;

import com.daon.backend.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FindMemberResponseDto {

    private String memberId;

    private String name;

    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public FindMemberResponseDto(Member member) {
        this.memberId = member.getId();
        this.name = member.getName();
        this.username = member.getUsername();
        this.createdAt = member.getCreatedAt();
        this.modifiedAt = member.getModifiedAt();
    }
}
