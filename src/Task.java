import java.util.Objects;

public class Task {
    String name;
    String description;
    int id;
    TaskStatus status;
    public Task (String name, String description, TaskStatus status){
        this.description = description;
        this.name = name;
        this.status = status;
    }
    public Task (String name, String description, TaskStatus status, int id){
        this.description = description;
        this.name = name;
        this.status = status;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return name.equals(task.name) && description.equals(task.description) && status.equals(task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
