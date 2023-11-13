package com.daon.backend.auth.service;

public interface MemberDetailsService {

    MemberDetails signIn(String email, String password);

}
