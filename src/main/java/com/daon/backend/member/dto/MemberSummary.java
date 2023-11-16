package com.daon.backend.member.dto;

import lombok.Getter;

@Getter
public class MemberSummary {

    private String username;

    private String name;

    public MemberSummary(String username, String name) {
        this.username = username;
        this.name = name;
    }
}
