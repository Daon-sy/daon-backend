package com.daon.backend.auth.service;

public interface MemberDetailsService {

    MemberDetails signIn(String username, String password);

}
