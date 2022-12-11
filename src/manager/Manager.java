package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.HashMap;
import java.util.List;

//Изменить название на IManager
public interface Manager {

  //  private final HashMap<Integer, Task> tasks = new HashMap<>();
   // private final HashMap<Integer, Epic> epics = new HashMap<>();
    //private final HashMap<Integer, Subtask> subTasks = new HashMap<>();
    //Integer countId = 0;

    public Integer addTask(Task task);

    public Integer addEpic(Epic epic);

    public Integer addSubTask(Subtask subtask);

    public List<Epic> getEpicList();

    public List<Subtask> getSubtaskList();

    public List<Task> getTaskList();

    public void clearAllTask();

    public void clearAllEpic();

    public void clearAllSubtask();

    public Task getTask(int number);
    public Epic getEpic(int number);

    public Subtask getSubtask(int number);

    public void updateTask(Task newTask);

    public void updateEpic(Epic newEpic);
    public void updateSubtask(Subtask newSubtask);

    public void removeTask(Integer id);
    public void removeEpic(Integer id);
    public void removeSubtask(Integer id);

    public List<Integer> getSubTask(int epicId);
}


