package com.daon.backend.task.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateTaskRequestDto {

    @NotBlank
    private String title;

    private String content;

    private Long taskManagerId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean emergency;

    private Long boardId;

}
