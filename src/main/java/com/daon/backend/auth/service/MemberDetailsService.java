package com.daon.backend.auth.service;

import com.daon.backend.member.dto.ModifyMemberDto;

import java.util.UUID;

public interface MemberDetailsService {

    MemberDetails signIn(String email, String password);

}
