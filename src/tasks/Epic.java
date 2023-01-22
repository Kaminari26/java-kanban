package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subTaskIds;

    public Epic(String name, TypeTask typeTask, String description) {
        super(name,typeTask, description, TaskStatus.NEW);
        subTaskIds = new ArrayList<>();
    }

    public Epic(String name, TypeTask typeTask, String description, TaskStatus status) {
        super(name,typeTask, description, status);
        subTaskIds = new ArrayList<>();
    }


    public Epic(int Id, String name, TypeTask typeTask, String description, TaskStatus status) {
        super(Id, typeTask, name, TaskStatus.NEW, description);
        subTaskIds = new ArrayList<>();
    }

    public void addSubtaskId(int id) {
        subTaskIds.add(id);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subTaskIds;
    }

    public void cleanSubtaskIds() {
        subTaskIds.clear();
    }

    public void removeSubtask(int id) {
        subTaskIds.remove(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return name.equals(epic.name)
                && description.equals(epic.description)
                && status.equals(epic.status)
                && id == epic.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }

    @Override
    public String toString() {
        return "tasks.Epic{" +
                "subList=" + subTaskIds +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

}



