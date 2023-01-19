package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public interface TaskManager {

    void addTask(Task task) throws IOException;

    void addEpic(Epic epic) throws IOException;

    void addSubTask(Subtask subtask) throws IOException;

    List<Epic> getEpicList();

    List<Subtask> getSubtaskList();

    List<Task> getTaskList();

    void clearAllTask() throws IOException;

    void clearAllEpic() throws IOException;

    void clearAllSubtask() throws IOException;

    Task getTask(int number) throws IOException;

    Epic getEpic(int number) throws IOException;

    Subtask getSubtask(int number) throws IOException;

    void updateTask(Task newTask);

    void updateEpic(Epic newEpic);

    void updateSubtask(Subtask newSubtask);

    void removeTask(Integer id) throws IOException;

    void removeEpic(Integer id) throws IOException;

    void removeSubtask(Integer id) throws IOException;

    List<Task> getHistory();

    List<Integer> getSubTask(int epicId);
}


