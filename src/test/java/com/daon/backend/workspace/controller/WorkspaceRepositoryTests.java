package com.daon.backend.workspace.controller;

import com.daon.backend.DaonBackendApplication;
import com.daon.backend.task.domain.Profile;
import com.daon.backend.task.domain.Workspace;
import com.daon.backend.task.infrastructure.ProfileJpaRepository;
import com.daon.backend.task.infrastructure.WorkspaceJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class WorkspaceRepositoryTests extends DaonBackendApplication {

    @Autowired
    private WorkspaceJpaRepository workspaceJpaRepository;

    @Autowired
    private ProfileJpaRepository profileJpaRepository;


    @DisplayName("워크스페이스 생성")
    @Test
    public void create() {

        Workspace workspace = new Workspace();
        Profile profile = new Profile();

        workspace.setName("워크스페이스test");
        workspace.setImageUrl("background");
        workspace.setDescription("안녕하세요. 워크스페이스입니다.");
        workspace.setSubject("프로젝트");

        profile.setNickname("김수완");
        profile.setImageUrl("profile");

        //Workspace newWorkspace = workspaceJpaRepository.save(workspace);
        //Profile newProfile = profileJpaRepository.save(profile);

    }

    /*@DisplayName("워크스페이스 목록")
    @Test
    public void read() {

    }*/

}