import java.util.ArrayList;
import java.util.Objects;

public class  Subtask extends Task {
    public Epic epic ;




    public Subtask(String name, String description, TaskStatus status, Epic epic)  {
        super(name, description, TaskStatus.NEW);
         this.epic = epic;

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return name.equals(subtask.name) && description.equals(subtask.description) && status.equals(subtask.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epic=" + epic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
