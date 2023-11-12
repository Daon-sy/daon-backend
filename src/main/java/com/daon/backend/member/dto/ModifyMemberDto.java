package com.daon.backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class ModifyMemberDto {

    private String email;
    private String password;
    private String name;

}
