package com.daon.backend.member.domain;

public interface PasswordEncoder {

    boolean match(String targetPassword, String encodedPassword);

    String encode(String password);
}
