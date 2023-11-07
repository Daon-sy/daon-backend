package com.daon.backend.auth.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberDetails {

    private String memberId;
    private String email;
    private String name;
}
