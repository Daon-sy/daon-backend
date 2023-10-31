package com.daon.backend.task.controller;

import com.daon.backend.task.domain.Workspace;
import com.daon.backend.task.dto.CreateProfileRequestDto;
import com.daon.backend.task.dto.CreateWorkspaceRequestDto;
import com.daon.backend.task.infrastructure.WorkspaceJpaRepository;
import com.daon.backend.task.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Autowired
    WorkspaceJpaRepository workspaceJpaRepository;

    @PostMapping("/workspaces")
    public ResponseEntity<Void> createWorkspace(@RequestBody CreateWorkspaceRequestDto createWorkspaceRequestDto) {
        workspaceService.createWorkspace(createWorkspaceRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/workspaces")
    public ResponseEntity<Void> createProfile(@RequestBody CreateProfileRequestDto createProfileRequestDto) {
        workspaceService.createProfile(createProfileRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/workspaces")
    public Workspace listWorkspace(@RequestParam UUID uuid) {
        Optional<Workspace> workspace = workspaceJpaRepository.findById(uuid);
        return workspace.get();
    }

}
