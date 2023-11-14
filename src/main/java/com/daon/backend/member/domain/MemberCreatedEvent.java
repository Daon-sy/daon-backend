package com.daon.backend.member.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberCreatedEvent {

    private String memberId;
    private String memberName;
    private String memberEmail;

    public static MemberCreatedEvent create(String memberId, String memberName, String memberEmail) {
        return new MemberCreatedEvent(memberId, memberName, memberEmail);
    }
}
