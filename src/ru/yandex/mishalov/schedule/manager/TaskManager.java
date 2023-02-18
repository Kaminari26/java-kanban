package ru.yandex.mishalov.schedule.manager;

import ru.yandex.mishalov.schedule.tasks.Epic;
import ru.yandex.mishalov.schedule.tasks.Subtask;
import ru.yandex.mishalov.schedule.tasks.Task;

import java.util.List;

public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(Subtask subtask);

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

    List<Integer> getSubTaskByEpicId(int epicId);

    void removeSubtask(Integer id);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    List<Subtask> getEpicSubtasks(int id);
}


