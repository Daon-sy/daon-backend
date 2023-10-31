package com.daon.backend.task.dto;

import com.daon.backend.task.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CreateProfileRequestDto {
    private String imageUrl;
    private String nickname;

    public Profile toEntity() {
        return Profile.builder()
                .nickname(nickname)
                .imageUrl(imageUrl)
                .build();
    }

}
