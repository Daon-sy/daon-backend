package com.daon.backend.task.service;

import com.daon.backend.config.MockConfig;
import com.daon.backend.task.domain.board.Board;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.dto.board.CreateBoardRequestDto;
import com.daon.backend.task.dto.board.FindBoardsResponseDto;
import com.daon.backend.task.dto.board.ModifyBoardRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class BoardServiceTest extends MockConfig {

    @MockBean
    SessionMemberProvider sessionMemberProvider;

    @Autowired
    BoardService boardService;

    @Autowired
    ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        BDDMockito.given(sessionMemberProvider.getMemberId()).willReturn("78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8");
    }

    @DisplayName("보드 생성")
    @Test
    void createBoard() {
        // given
        Long projectId = 1L;
        CreateBoardRequestDto requestDto = new CreateBoardRequestDto("보드 제목");

        // when
        boardService.createBoard(projectId, requestDto);

        // then
        Project project = projectRepository.findProjectById(projectId).orElseThrow();
        assertThat(project.getBoards().size()).isEqualTo(3);
    }

    @DisplayName("보드 목록 조회")
    @Test
    void findBoards() {
        // given
        Long projectId = 1L;

        // when
        FindBoardsResponseDto boards = boardService.findBoards(projectId);

        // then
        assertThat(boards.getBoards().size()).isEqualTo(2);
    }

    @DisplayName("보드 수정")
    @Test
    void modifyBoard() {
        // given
        Long projectId = 1L;
        Long boardId = 1L;
        String editTitle = "수정된 제목";

        ModifyBoardRequestDto requestDto = new ModifyBoardRequestDto(editTitle);

        // when
        boardService.modifyBoard(projectId, boardId, requestDto);

        // then
        Board findBoard = projectRepository.findProjectById(projectId).orElseThrow().getBoards().get(0);
        assertThat(findBoard.getId()).isEqualTo(boardId);
        assertThat(findBoard.getTitle()).isEqualTo(editTitle);
    }

    @DisplayName("보드 삭제")
    @Test
    void deleteBoard() {
        // given
        Long projectId = 1L;
        Long boardId = 2L;

        // when
        boardService.deleteBoard(projectId, boardId);

        // then
        int boardSize = boardService.findBoards(projectId).getBoards().size();
        assertThat(boardSize).isEqualTo(1);
    }
}
