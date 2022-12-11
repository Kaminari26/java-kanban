package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements Manager {
    inMemoryHistoryManager inMemoryHistoryManager = new inMemoryHistoryManager();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subTasks = new HashMap<>();

    Integer countId = 0;
    @Override

    public Integer addTask(Task task) {
        if (task == null) {
            return null;
        }
        countId++;
        task.setId(countId);
        tasks.put(countId, task);
        return countId;
    }

    @Override
    public Integer addEpic(Epic epic) {
        countId++;
        epic.setId(countId);
        epics.put(countId, epic);
        return countId;
    }

    @Override
    public Integer addSubTask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return null;
        }
        countId++;
        subtask.setId(countId);
        subTasks.put(countId, subtask);
        epics.get(subtask.getEpicId()).addSubtaskId(countId);
        checkStatusEpic(subtask.getEpicId());
        return countId;
    }

    @Override
    public List<Epic> getEpicList() {
        return List.copyOf(epics.values());
    }

    @Override
    public List<Subtask> getSubtaskList() {
        return List.copyOf((subTasks.values()));
    }

    @Override
    public List<Task> getTaskList() {
        return List.copyOf(tasks.values());
    }

    @Override
    public void clearAllTask() {
        tasks.clear();
    }

    @Override
    public void clearAllEpic() {
        clearAllSubtask();
        epics.clear();
    }

    @Override
    public void clearAllSubtask() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            checkStatusEpic(epic.getId());
        }
        subTasks.clear();
    }

    @Override
    public Task getTask(int number) {
          inMemoryHistoryManager.add(tasks.get(number));
        return tasks.get(number);
    }

    @Override
    public Epic getEpic(int number) {
        inMemoryHistoryManager.add(epics.get(number));
        return epics.get(number);
    }

    @Override
    public Subtask getSubtask(int number) {
        inMemoryHistoryManager.add(subTasks.get(number));
        return subTasks.get(number);
    }

    @Override
    public void updateTask(Task newTask) {
        if (newTask == null) {
            return;
        }
        if (!tasks.containsKey(newTask.getId())) {
            return;
        }
        tasks.put(newTask.getId(), newTask);
    }

    @Override
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

    @Override
    public void updateSubtask(Subtask newSubtask) {
        if (newSubtask == null) {
            return;
        }
        if (!subTasks.containsKey(newSubtask.getId())) {
            return;
        }
        subTasks.put(newSubtask.getId(), newSubtask);
        checkStatusEpic(newSubtask.getEpicId());
    }

    @Override
    public void removeTask(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpic(Integer id) {
        for (Integer subTaskId : epics.get(id).getSubtaskIds()) {
            subTasks.remove(subTaskId);
        }

        epics.remove(id);
    }

    @Override
    public void removeSubtask(Integer id) {
        epics.get(subTasks.get(id).getEpicId()).removeSubtask(id);
        subTasks.remove(id);
        checkStatusEpic(subTasks.get(id).getEpicId());
    }

    @Override
    public List<Integer> getSubTask(int epicId) {
        return List.copyOf(epics.get(epicId).getSubtaskIds());
    }

    private void checkStatusEpic(Integer epicId) {
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
    }
}


