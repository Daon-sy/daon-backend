package com.daon.backend.task.dto.task;

import com.daon.backend.task.domain.task.TaskProgressStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyTaskRequestDto {

    @NotBlank
    @Size(max = 20)
    private String title;

    @Size(max = 1000)
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;

    private boolean emergency;

    private TaskProgressStatus progressStatus;

    @NotNull
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
