package com.daon.backend.member.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
public class CheckUsernameRequestDto {

    @NotBlank
    @Length(max = 20)
    private String username;
}
