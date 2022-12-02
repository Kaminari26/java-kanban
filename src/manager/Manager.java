package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {

    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subTaskHashMap = new HashMap<>();
    Integer countId = 0;
    //HashMap<String, List<Integer>> mapForMethod = new HashMap<>();

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
        ArrayList<Subtask> sTusk = new ArrayList<>();

        for (Integer id : mapForMethod.get(subTaskClassName)) {
            sTusk.add((Subtask) taskHashMap.get(id));
        }
        return sTusk;
    }

    public List<Task> getTaskList() {
        ArrayList<Task> aTask = new ArrayList<>();

        for (Integer id : mapForMethod.get(taskClassName)) {
            aTask.add(taskHashMap.get(id));
        }
        return aTask;
    }

    public void clearAllTask() {
        taskHashMap.clear();
    }

    public void clearAllEpic() {
        for (Integer id : mapForMethod.get(epicClassName)) {
            taskHashMap.remove(id);
        }
        mapForMethod.remove(epicClassName);
    }

    public void clearAllSubtask() {
        for (Integer id : mapForMethod.get(subTaskClassName)) {
            taskHashMap.remove(id);
        }
        mapForMethod.remove(subTaskClassName);
    }

    public Task getTask(int number) {

        return taskHashMap.get(number);
    }

    public Epic getEpic(int number) {

        return taskHashMap.get(number);
    }

    public Subtask getSubtask(int number) {

        return taskHashMap.get(number);
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

    public void updateEpic(Epic epic) {
        taskHashMap.put(epic.id, epic);
        checkStatusEpic(epic);
    }

    public void updateSubtask(Subtask subtask) {

        taskHashMap.put(subtask.id, subtask);

        checkStatusEpic(subtask.epic);
    }

    public void removeTask(Integer id) {
        taskHashMap.remove(id);
    }

    public void removeEpic(Integer number) {
        taskHashMap.remove(number);
        mapForMethod.get(epicClassName).remove(number);
    }

    public void removeSubtask(Integer number) {
        taskHashMap.remove(number);
        mapForMethod.get(subTaskClassName).remove(number);
    }

    public ArrayList<Subtask> epicAndCo(int id) {

        return ((Epic) taskHashMap.get(id)).subList;
    }

    public void checkStatusEpic(Epic epic) {
        int done = 0;
        int inProgress = 0;
        for (Subtask list : epic.subList) {
            if (list.status == TaskStatus.DONE) {
                done = done + 1;
            } else if (list.status == TaskStatus.IN_PROGRESS) {
                inProgress = inProgress + 1;
            }
        }
        if (inProgress < 0) {
            epic.status = TaskStatus.IN_PROGRESS;
        } else if (done == epic.subList.size()) {
            epic.status = TaskStatus.DONE;
        } else {
            epic.status = TaskStatus.NEW;
        }
    }
}


