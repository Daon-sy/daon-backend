package com.daon.backend.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class AddEmailRequestDto {

    @Email
    @NotBlank
    private String email;

}
