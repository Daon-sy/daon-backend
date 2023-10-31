package com.daon.backend.task.service;

import com.daon.backend.member.dto.SignUpRequestDto;
import com.daon.backend.member.infrastructure.MemberJpaRepository;
import com.daon.backend.task.dto.CreateProfileRequestDto;
import com.daon.backend.task.dto.CreateWorkspaceRequestDto;
import com.daon.backend.task.infrastructure.ProfileJpaRepository;
import com.daon.backend.task.infrastructure.WorkspaceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class WorkspaceService {

    private final WorkspaceJpaRepository workspaceJpaRepository;
    private final ProfileJpaRepository profileJpaRepository;

    public UUID createWorkspace(CreateWorkspaceRequestDto createWorkspaceRequestDto) {
        return workspaceJpaRepository.save(createWorkspaceRequestDto.toEntity()).getId();
    }

    public UUID createProfile(CreateProfileRequestDto createProfileRequestDto) {
        return profileJpaRepository.save(createProfileRequestDto.toEntity()).getId();
    }

}
