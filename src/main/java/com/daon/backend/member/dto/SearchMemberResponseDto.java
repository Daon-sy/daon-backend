package com.daon.backend.member.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchMemberResponseDto {

    private List<MemberSummary> members;

    public SearchMemberResponseDto(List<MemberSummary> members) {
        this.members = members;
    }
}
