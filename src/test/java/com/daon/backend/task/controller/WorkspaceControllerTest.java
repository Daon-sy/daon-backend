package com.daon.backend.task.controller;

import com.daon.backend.task.domain.workspace.Role;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceCreator;
import com.daon.backend.task.dto.request.CheckJoinCodeRequestDto;
import com.daon.backend.task.dto.request.CreateWorkspaceRequestDto;
import com.daon.backend.task.dto.request.JoinWorkspaceRequestDto;
import com.daon.backend.task.dto.response.FindParticipantsResponseDto;
import com.daon.backend.task.dto.response.FindProfileResponseDto;
import com.daon.backend.task.dto.response.JoinWorkspaceResponseDto;
import com.daon.backend.task.dto.response.WorkspaceListResponseDto;
import com.daon.backend.task.service.WorkspaceService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class WorkspaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private WorkspaceService workspaceService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @DisplayName("createWorkspace(): 워크스페이스 생성")
    @Test
    void createWorkspace() throws Exception {
        // given
        final String url = "/api/workspaces";
        final Long workspaceId = 1L;

        CreateWorkspaceRequestDto.WorkspaceInfo workspaceInfo =
                CreateWorkspaceRequestDto.WorkspaceInfo.builder()
                        .title("테스트 워크스페이스")
                        .imageUrl("https://xxx.xxxx.xxxx")
                        .subject("테스트")
                        .description("테스트 용도로 생성")
                        .build();
        CreateWorkspaceRequestDto.WorkspaceProfileInfo workspaceProfileInfo =
                CreateWorkspaceRequestDto.WorkspaceProfileInfo.builder()
                        .name("홍길동")
                        .imageUrl("https://xxx.xxxx.xxxx")
                        .email("test@gmail.com")
                        .build();
        CreateWorkspaceRequestDto requestDto = new CreateWorkspaceRequestDto(workspaceInfo, workspaceProfileInfo);

        given(workspaceService.createWorkspace(Mockito.any(CreateWorkspaceRequestDto.class)))
                .willReturn(workspaceId);

        final String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.workspaceId").value(workspaceId));
    }

    @DisplayName("workspaceList(): 워크스페이스 목록 조회")
    @Test
    void workspaceList() throws Exception {
        // given
        final String url = "/api/workspaces";
        final String memberId = "uuid";

        WorkspaceCreator creator = WorkspaceCreator.builder()
                .memberId(memberId)
                .profileName("홍길동")
                .build();

        Workspace workspace1 = Workspace.createOfPersonal(creator);
        Workspace workspace2 = Workspace.createOfGroup(
                "테스트 워크스페이스",
                "테스트용 워크스페이스입니다",
                "https://xxx.xxxx.xxxx",
                "테스트",
                creator
        );

        List<WorkspaceListResponseDto.WorkspaceSummary> workspaceList =
                Arrays.asList(
                        new WorkspaceListResponseDto.WorkspaceSummary(workspace1),
                        new WorkspaceListResponseDto.WorkspaceSummary(workspace2)
                );

        given(workspaceService.findAllWorkspace())
                .willReturn(new WorkspaceListResponseDto(workspaceList));

        // when
        ResultActions result = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON_VALUE));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalCount").value(2))
                .andExpect(jsonPath("$.data.workspaces[0].title").value("홍길동님의 개인 워크스페이스 공간"))
                .andExpect(jsonPath("$.data.workspaces[0].division").value("PERSONAL"))
                .andExpect(jsonPath("$.data.workspaces[1].title").value("테스트 워크스페이스"))
                .andExpect(jsonPath("$.data.workspaces[1].imageUrl").value("https://xxx.xxxx.xxxx"))
                .andExpect(jsonPath("$.data.workspaces[1].division").value("GROUP"));
    }

    @DisplayName("checkJoinCode(): 참여코드 확인")
    @Test
    void checkJoinCode() throws Exception {
        // given
        final String url = "/api/workspaces/code";
        final String joinCode = "joinCode";

        CheckJoinCodeRequestDto requestDto = new CheckJoinCodeRequestDto(joinCode);
        doNothing().when(workspaceService).checkJoinCode(requestDto);

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody)
        );

        // then
        result.andExpect(status().isOk());
    }

    @DisplayName("joinWorkspace(): 워크스페이스 참여")
    @Test
    void joinWorkspace() throws Exception {
        // given
        final String url = "/api/workspaces/join";
        final String joinCode = "joinCode";
        final Long workspaceId = 1L;

        JoinWorkspaceRequestDto requestDto = new JoinWorkspaceRequestDto(
                joinCode,
                new JoinWorkspaceRequestDto.WorkspaceProfileInfo(
                        "홍길동",
                        "https://xxx.xxxx.xxxx",
                        "test@gmail.com"
                )
        );
        JoinWorkspaceResponseDto responseDto = new JoinWorkspaceResponseDto(workspaceId);
        given(workspaceService.joinWorkspace(Mockito.any(JoinWorkspaceRequestDto.class)))
                .willReturn(responseDto);

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody)
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.workspaceId").value(workspaceId));


    }

    @DisplayName("findProfile(): 프로필 조회")
    @Test
    void findProfile() throws Exception {
        final String url = "/api/workspaces/{workspaceId}/profile/me";
        final Long workspaceId = 1L;

        FindProfileResponseDto responseDto = new FindProfileResponseDto(
                1L,
                "홍길동",
                "https://xxx.xxxx.xxxx",
                "test@gmail.com",
                Role.BASIC_PARTICIPANT
        );

        given(workspaceService.findProfile(Mockito.anyLong()))
                .willReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(get(url, workspaceId)
                .contentType(APPLICATION_JSON_VALUE));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.participantId").value(1L))
                .andExpect(jsonPath("$.data.name").value("홍길동"))
                .andExpect(jsonPath("$.data.imageUrl").value("https://xxx.xxxx.xxxx"))
                .andExpect(jsonPath("$.data.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.data.role").value("BASIC_PARTICIPANT"));
    }

    @DisplayName("findParticipants(): 워크스페이스 구성원 목록 조회")
    @Test
    void findParticipants() throws Exception {
        // given
        final String url = "/api/workspaces/{workspaceId}/participants";
        final Long workspaceId = 1L;

        FindParticipantsResponseDto responseDto = new FindParticipantsResponseDto(
                List.of(
                        new FindParticipantsResponseDto.ParticipantProfile(
                                workspaceId,
                                "홍길동",
                                "test@gmail.com",
                                "https://xxx.xxxx.xxxx",
                                Role.BASIC_PARTICIPANT
                        )
                )
        );

        given(workspaceService.findParticipants(workspaceId))
                .willReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(get(url, workspaceId)
                .contentType(APPLICATION_JSON_VALUE));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalCount").value(1))
                .andExpect(jsonPath("$.data.participants[0].participantId").value(1L))
                .andExpect(jsonPath("$.data.participants[0].name").value("홍길동"))
                .andExpect(jsonPath("$.data.participants[0].imageUrl").value("https://xxx.xxxx.xxxx"))
                .andExpect(jsonPath("$.data.participants[0].email").value("test@gmail.com"))
                .andExpect(jsonPath("$.data.participants[0].role").value("BASIC_PARTICIPANT"));
    }
}