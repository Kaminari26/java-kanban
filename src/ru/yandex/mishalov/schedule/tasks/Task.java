package ru.yandex.mishalov.schedule.tasks;

import ru.yandex.mishalov.schedule.helpers.LocalDateTimeHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;
    protected TypeTask type;
    protected LocalDateTime startTime;
    protected Long duration;

    public Task(String name, TypeTask type, String description, TaskStatus status) {
        this.description = description;
        this.name = name;
        this.status = status;
        this.type = type;
//        this.duration = 0l;
//        LocalDateTime localDateTime = LocalDateTimeHelper.convertToLocalDateTime("14.02.2023 18:01");
//        localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
//        this.startTime = localDateTime;
    }

    public Task(int id, TypeTask type, String name, TaskStatus status, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(int id, TypeTask type, String name, TaskStatus status, String description, LocalDateTime startTime, Long duration) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Long getDuration() {
        if(duration == null)
        {
            return 0l;
        }
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
            return startTime;

    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if(startTime != null) {
            return startTime.plusMinutes(duration);
        }else {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeTask getType() {
        return type;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return name.equals(task.name) && description.equals(task.description) &&
                status.equals(task.status) && id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }

    @Override
    public String toString() {
        return "tasks.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
    public int compareByTime(Task task){
        if (this.startTime == null){
            return 1;
        } else if (task.getStartTime() == null) {
            return -1;
        } else if (this.startTime == null && task.getStartTime() == null) {
            return 1;
        } else if (this.startTime.isAfter(task.getStartTime())) {
            return 1;
        }
        return -1;
    }
}

