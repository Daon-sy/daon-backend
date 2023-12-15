package com.daon.backend.notification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TasksNotificationParams {

    private Long projectId;

    private Long boardId;

    private boolean bookmarked;

    private boolean my;

    public boolean checkValidRequest() {
        return projectId != null || boardId != null || bookmarked || my;
    }

    public String getSuffixByParam() {
        if (bookmarked) {
            return "_bookmark_";
        } else if (my) {
            return "_my_";
        } else if (boardId != null) {
            return "_board_" + boardId + "_project_" + projectId + "_";
        } else {
            return "_project_" + projectId + "_";
        }
    }
}
