package com.daon.backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class ModifyMemberDto {

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;



}
