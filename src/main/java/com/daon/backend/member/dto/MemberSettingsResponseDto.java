package com.daon.backend.member.dto;

import lombok.Getter;

@Getter
public class MemberSettingsResponseDto {

    private boolean notified;

    public MemberSettingsResponseDto(boolean notified) {
        this.notified = notified;
    }
}
