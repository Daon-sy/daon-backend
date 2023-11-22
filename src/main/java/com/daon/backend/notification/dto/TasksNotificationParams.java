package com.daon.backend.notification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TasksNotificationParams {

    private Long projectId;

    private Long boardId;

    private boolean bookmark;

    private boolean my;

    public boolean checkValidRequest() {
        return projectId != null || boardId != null || bookmark || my;
    }

    public String getSuffixByParam() {
        if (bookmark) {
            return "_bookmark_";
        } else if (my) {
            return "_my_";
        } else if (boardId != null) {
            return "_board_" + boardId + "_project_" + projectId + "_"; // tasks_1_board_1_project_3
        } else {
            return "_project_" + projectId + "_";
        }
    }
}
