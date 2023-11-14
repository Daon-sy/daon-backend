package com.daon.backend.task.dto.request;

import com.daon.backend.task.domain.project.TaskProgressStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ModifyTaskRequestDto {

    @NotBlank
    private String title;

    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private boolean emergency;

    private TaskProgressStatus progressStatus;

    private Long boardId;

    private Long taskManagerId;

    public LocalDateTime getStartDate() {
        if (startDate == null) {
            return null;
        }
        return LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
    }

    public LocalDateTime getEndDate() {
        if (endDate == null) {
            return null;
        }
        return LocalDateTime.of(endDate, LocalTime.of(0, 0, 0));
    }
}