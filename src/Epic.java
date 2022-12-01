import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task{
   public ArrayList<Subtask> subList;

    public Epic(String name, String description,TaskStatus status) {
        super(name, description, TaskStatus.NEW);
       subList = new ArrayList<>();

    }
    public Epic(String name, String description, TaskStatus status, int id) {
        super(name, description, TaskStatus.NEW, id);

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return name.equals(epic.name) && description.equals(epic.description) && status.equals(epic.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subList=" + subList +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}



