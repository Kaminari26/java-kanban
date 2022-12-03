package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.HashMap;
import java.util.List;

public class Manager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subTasks = new HashMap<>();
    Integer countId = 0;

    public Integer addTask(Task task) {
        if (task == null) {
            return null;
        }
        countId++;
        task.setId(countId);
        tasks.put(countId, task);
        return countId;
    }

    public Integer addEpic(Epic epic) {
        countId++;
        epic.setId(countId);
        epics.put(countId, epic);
        return countId;
    }

    public Integer addSubTask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return null;
        }
        countId++;
        subtask.setId(countId);
        subTasks.put(countId, subtask);
        epics.get(subtask.getEpicId()).addSubtaskId(countId);
        checkStatusEpic(epics.get(subtask.getEpicId()));
        return countId;
    }

    public List<Epic> getEpicList() {
        return List.copyOf(epics.values());
    }

    public List<Subtask> getSubtaskList() {
        return List.copyOf((subTasks.values()));
    }

    public List<Task> getTaskList() {
        return List.copyOf(tasks.values());
    }

    public void clearAllTask() {
        tasks.clear();
    }

    public void clearAllEpic() {
        epics.clear();
        clearAllSubtask();
    }

    public void clearAllSubtask() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            checkStatusEpic(epic);
        }
        subTasks.clear();
    }

    public Task getTask(int number) {
        return tasks.get(number);
    }

    public Epic getEpic(int number) {

        return epics.get(number);
    }

    public Subtask getSubtask(int number) {

        return subTasks.get(number);
    }

    public void updateTask(Task newTask) {
        if (newTask == null) {
            return;
        }
        if (!tasks.containsKey(newTask.getId())) {
            return;
        }
        tasks.put(newTask.getId(), newTask);
    }

    public void updateEpic(Epic newEpic) {
        if (newEpic == null) {
            return;
        }
        if (!epics.containsKey(newEpic.getId())) {
            return;
        }
        Epic oldEpic = epics.get(newEpic.getId());
        oldEpic.setName(newEpic.getName());
        oldEpic.setDescription(newEpic.getDescription());
    }

    public void updateSubtask(Subtask newSubtask) {
        if (newSubtask == null) {
            return;
        }
        if (!subTasks.containsKey(newSubtask.getId())) {
            return;
        }
        subTasks.put(newSubtask.getId(), newSubtask);
        checkStatusEpic(epics.get(newSubtask.getId()));
    }

    public void removeTask(Integer id) {
        tasks.remove(id);
    }

    public void removeEpic(Integer id) {
        for (Subtask subtasks : subTasks.values()) {
            if (subtasks.getEpicId() == id) {
                subTasks.remove(subtasks.getId());
            }
        }
        epics.remove(id);
    }

    public void removeSubtask(Integer id) {
        subTasks.remove(id);
        checkStatusEpic(epics.get(subTasks.get(id).getEpicId()));
        epics.get(subTasks.get(id).getEpicId()).removeSubtask(id);
    }

    public List<Integer> getSubTask(int epicId) {
        return List.copyOf(epics.get(epicId).getSubtaskIds());
    }

    public void checkStatusEpic(Epic epic) {
        int done = 0;
        int inProgress = 0;
        for (Subtask subtask : getSubtaskList()) {
            if (subTasks.get(subtask.getId()).getStatus() == TaskStatus.DONE) {
                done = done + 1;
            } else if (subTasks.get(subtask.getId()).getStatus() == TaskStatus.IN_PROGRESS) {
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


