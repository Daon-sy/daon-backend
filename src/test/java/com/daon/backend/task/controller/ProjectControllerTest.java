package com.daon.backend.task.controller;

import com.daon.backend.task.dto.request.CreateProjectRequestDto;
import com.daon.backend.task.dto.request.InviteWorkspaceParticipantRequestDto;
import com.daon.backend.task.dto.response.CreateProjectResponseDto;
import com.daon.backend.task.dto.response.ProjectListResponseDto;
import com.daon.backend.task.infrastructure.CheckRoleInterceptor;
import com.daon.backend.task.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private CheckRoleInterceptor interceptor;

    @BeforeEach
    void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        when(interceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @DisplayName("createProject(): 프로젝트 생성")
    @Test
    void createProject() throws Exception {
        // given
        final String url = "/api/workspaces/{workspaceId}/projects";
        final Long workspaceId = 1L;
        final Long projectId = 1L;

        CreateProjectRequestDto requestDto = new CreateProjectRequestDto(
                "프로젝트 이름",
                "프로젝트 설명"
        );
        CreateProjectResponseDto responseDto = new CreateProjectResponseDto(projectId);

        given(projectService.createProject(Mockito.anyLong(), Mockito.any(CreateProjectRequestDto.class)))
                .willReturn(projectId);

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions result = mockMvc.perform(post(url, workspaceId)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.projectId").value(responseDto.getProjectId()));
    }

    @DisplayName("projectList(): 프로젝트 목록 조회")
    @Test
    void projectList() throws Exception {
        // given
        final String url = "/api/workspaces/{workspaceId}/projects";
        final Long workspaceId = 1L;
        final Long projectId = 1L;

        ProjectListResponseDto responseDto = new ProjectListResponseDto(
                workspaceId,
                List.of(
                        new ProjectListResponseDto.ProjectSummary(
                                projectId,
                                "프로젝트 이름",
                                "프로젝트 설명"
                        )
                )
        );

        given(projectService.findAllProjectInWorkspace(Mockito.anyLong()))
                .willReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(get(url, workspaceId)
                .contentType(APPLICATION_JSON_VALUE));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.workspaceId").value(workspaceId))
                .andExpect(jsonPath("$.data.totalCount").value(responseDto.getTotalCount()))
                .andExpect(jsonPath("$.data.projects[0].projectId").value(responseDto.getProjects().get(0).getProjectId()))
                .andExpect(jsonPath("$.data.projects[0].projectName").value(responseDto.getProjects().get(0).getTitle()))
                .andExpect(jsonPath("$.data.projects[0].description").value(responseDto.getProjects().get(0).getDescription()));
    }

    @DisplayName("inviteWorkspaceParticipant(): 프로젝트 초대")
    @Test
    void inviteWorkspaceParticipant() throws Exception {
        // given
        final String url = "/api/workspaces/{workspaceId}/projects/{projectId}/invite";
        final Long workspaceId = 1L;
        final Long projectId = 1L;
        final Long workspaceParticipantId = 1L;

        InviteWorkspaceParticipantRequestDto requestDto = new InviteWorkspaceParticipantRequestDto(workspaceParticipantId);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions result = mockMvc.perform(post(url, workspaceId, projectId)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isOk());
    }
}