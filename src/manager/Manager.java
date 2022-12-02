package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.HashMap;
import java.util.List;

public class Manager {

    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subTaskHashMap = new HashMap<>();
    Integer countId = 0;

    public Integer addTask(Task task) {
        if (task == null) {
            return null;
        }
        countId++;
        task.setId(countId);
        taskHashMap.put(countId, task);
        return countId;
    }

    public Integer addEpic(Epic epic) {
        countId++;
        epic.setId(countId);
        epicHashMap.put(countId, epic);
        return countId;
    }

    public Integer addSubask(Subtask subtask) {
        if (!epicHashMap.containsKey(subtask.getEpicId())) {
            return null;
        }
        countId++;
        subtask.setId(countId);
        subTaskHashMap.put(countId, subtask);
        epicHashMap.get(subtask.getEpicId()).addSubtaskId(countId);
        return countId;

    }

    public List<Epic> getEpicList() {
        return List.copyOf(epicHashMap.values());
    }

    public List<Subtask> getSubtaskList() {
        return List.copyOf((subTaskHashMap.values()));
    }

    public List<Task> getTaskList() {
        return List.copyOf(taskHashMap.values());
    }

    public void clearAllTask() {
        taskHashMap.clear();
    }

    public void clearAllEpic() {
        epicHashMap.clear();
    }

    public void clearAllSubtask() {
        subTaskHashMap.clear();
    }

    public Task getTask(int number) {
        return taskHashMap.get(number);
    }

    public Epic getEpic(int number) {

        return epicHashMap.get(number);
    }

    public Subtask getSubtask(int number) {

        return subTaskHashMap.get(number);
    }

    public void updateTask(Task newTask) {
        if (newTask == null) {
            return;
        }
        if (!taskHashMap.containsKey(newTask.getId())) {
            return;
        }
        Task oldTask = taskHashMap.get(newTask.getId());
        oldTask.setName(newTask.getName());
        oldTask.setDescription(newTask.getDescription());
    }

    public void updateEpic(Epic newEpic) {
        if(newEpic == null){
            return;
        }
        if (!epicHashMap.containsKey(newEpic.getId())){
            return;
        }
        Epic oldEpic = epicHashMap.get(newEpic.getId());
        oldEpic.setName(newEpic.getName());
        oldEpic.setDescription(newEpic.getDescription());
    }

    public void updateSubtask(Subtask newSubtask) {
        if(newSubtask == null){
            return;
        }
        if (!subTaskHashMap.containsKey(newSubtask.getId())){
            return;
        }
        Subtask oldSabtask = subTaskHashMap.get(newSubtask.getId());
        oldSabtask.setName(newSubtask.getName());
        oldSabtask.setDescription(newSubtask.getDescription());
    }

    public void removeTask(Integer id) {
        taskHashMap.remove(id);
    }

    public void removeEpic(Integer id) {
        epicHashMap.remove(id);
    }

    public void removeSubtask(Integer id) {
       subTaskHashMap.remove(id);
    }

    public List<Integer> epicAndCo(int id) {

        return List.copyOf(epicHashMap.get(id).getSubtaskIds());
    }

    public void checkStatusEpic(Epic epic) {
        int done = 0;
        int inProgress = 0;
        for (Subtask list : getSubtaskList()) {
            if (subTaskHashMap.get(list).getStatus() == TaskStatus.DONE) {
                done = done + 1;
            } else if (subTaskHashMap.get(list).getStatus() == TaskStatus.IN_PROGRESS) {
                inProgress = inProgress + 1;
            }
        }
        if (inProgress < 0) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else if (done == epic.getSubtaskIds().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }
}


