package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {


    Integer addTask(Task task);

    Integer addEpic(Epic epic);

    Integer addSubTask(Subtask subtask);

    List<Epic> getEpicList();

    List<Subtask> getSubtaskList();

    List<Task> getTaskList();

    void clearAllTask();

    void clearAllEpic();

    void clearAllSubtask();

    Task getTask(int number);

    Epic getEpic(int number);

    Subtask getSubtask(int number);

    void updateTask(Task newTask);

    void updateEpic(Epic newEpic);

    void updateSubtask(Subtask newSubtask);

    void removeTask(Integer id);

    void removeEpic(Integer id);

    void removeSubtask(Integer id);

    List<Task> getHistory();

    List<Integer> getSubTask(int epicId);
}


