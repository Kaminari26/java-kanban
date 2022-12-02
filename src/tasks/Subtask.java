package tasks;

import java.util.Objects;

public class  Subtask extends Task {

    private Integer epicId ;

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, Integer epicId)  {
        super(name, description, TaskStatus.NEW);
         this.epicId = epicId;

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return name.equals(subtask.name)
                && description.equals(subtask.description)
                && status.equals(subtask.status)
                && id == subtask.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }

    @Override
    public String toString() {
        return "tasks.Subtask{" +
                "epic=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
