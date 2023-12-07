package com.daon.backend.task.dto.workspace;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchMemberResponseDto {

    private List<MemberSummary> members;

    public SearchMemberResponseDto(List<MemberSummary> members) {
        this.members = members;
    }

    @Getter
    public static class MemberSummary {

        private String username;

        private String name;

        private boolean invited;

        public MemberSummary(String username, String name, boolean invited) {
            this.username = username;
            this.name = name;
            this.invited = invited;
        }
    }
}
