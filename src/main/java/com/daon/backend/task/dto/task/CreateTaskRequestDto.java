package com.daon.backend.task.dto.task;

import com.daon.backend.task.domain.task.TaskProgressStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class CreateTaskRequestDto {

    @NotBlank
    @Size(max = 20)
    private String title;

    @Size(max = 1000)
    private String content;

    private Long taskManagerId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private boolean emergency;

    private Long boardId;

    @NotBlank
    private TaskProgressStatus progressStatus;

    public LocalDateTime getStartDate() {
        if (startDate == null) return null;
        return LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
    }

    public LocalDateTime getEndDate() {
        if (endDate == null) return null;
        return LocalDateTime.of(endDate, LocalTime.of(0, 0, 0));
    }
}
