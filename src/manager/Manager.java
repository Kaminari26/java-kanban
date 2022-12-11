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

  /*  private void checkStatusEpic(Integer epicId) {
        Epic epic = epics.get(epicId);
        int done = 0;
        int inProgress = 0;

        for (Integer subTaskId : epic.getSubtaskIds()) {
            TaskStatus subTaskStatus = subTasks.get(subTaskId).getStatus();

            if (subTaskStatus == TaskStatus.DONE) {
                done = done + 1;
            } else if (subTaskStatus == TaskStatus.IN_PROGRESS) {
                inProgress = inProgress + 1;
            }
        }

        if (inProgress < 0) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else if (done > 0 && done == epic.getSubtaskIds().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    } */
}


