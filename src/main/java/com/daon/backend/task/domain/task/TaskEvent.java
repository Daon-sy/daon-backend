package com.daon.backend.task.domain.task;

import lombok.Getter;

public class TaskEvent {

    public static Created created(Task task) {
        return new Created(task);
    }

    public static Modified modified(Task task) {
        return new Modified(task);
    }

    public static Removed removed(Task task) {
        return new Removed(task);
    }

    @Getter
    public static class Created {
        private Task task;

        public Created(Task task) {
            this.task = task;
        }
    }

    @Getter
    public static class Modified {
        private Task task;

        public Modified(Task task) {
            this.task = task;
        }
    }

    @Getter
    public static class Removed {
        private Task task;

        public Removed(Task task) {
            this.task = task;
        }
    }
}
