package ru.yandex.mishalov.schedule.manager;

import ru.yandex.mishalov.schedule.tasks.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskTaskManager implements TaskManager {

    protected final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(Task::compareByTime);

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subTasks = new HashMap<>();
    protected Integer countId = 0;

    @Override
    public void addTask(Task task) {
            if (task.getType().equals(TypeTask.TASK)) {
                countId++;
                task.setId(countId);
                tasks.put(countId, task);
            }
        addToPrioritizedSet(task);
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic.getType().equals(TypeTask.EPIC)) {
            countId++;
            epic.setId(countId);
            epics.put(countId, epic);
        }
        addToPrioritizedSet(epic);
    }

    @Override
    public void addSubTask(Subtask subtask) {
        if (subtask.getType().equals(TypeTask.SUBTASK)) {
            if (epics.containsKey(subtask.getEpicId())) {
                    countId++;
                    subtask.setId(countId);
                    subTasks.put(countId, subtask);
                    epics.get(subtask.getEpicId()).addSubtaskId(countId);
                    updateEpic(subtask.getEpicId());
                }
            }
        addToPrioritizedSet(subtask);
        }

    @Override
    public List<Epic> getEpicList() {
        return List.copyOf(epics.values());
    }

    protected HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    @Override
    public List<Subtask> getSubtaskList() {//+
        return List.copyOf((subTasks.values()));
    }

    @Override
    public List<Task> getTaskList() {
        return List.copyOf(tasks.values());
    }

    @Override
    public void clearAllTask(){
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
            updateEpic(epic.getId());
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
        addToPrioritizedSet(newTask);
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
        updateEpic(newSubtask.getEpicId());
        addToPrioritizedSet(newSubtask);
    }

    @Override
    public void removeTask(Integer id) {
        inMemoryHistoryManager.remove(id);
        delete(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void removeEpic(Integer id) {
        for (Integer subTaskId : epics.get(id).getSubtaskIds()) {
            subTasks.remove(subTaskId);
            inMemoryHistoryManager.remove(subTaskId);
        }

        epics.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public List<Integer> getSubTaskByEpicId(int epicId) {
        return List.copyOf(epics.get(epicId).getSubtaskIds());
    }

    @Override
    public void removeSubtask(Integer id) {
        Integer epicId = subTasks.get(id).getEpicId();
        epics.get(epicId).removeSubtask(id);
        delete(subTasks.get(id));
        subTasks.remove(id);
        inMemoryHistoryManager.remove(id);
        checkStatusEpic(epicId);
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    private void updateEpicDuration(Epic epic) {
        List<Integer> subs = epic.getSubtaskIds();
        if (subs.isEmpty()) {
            epic.setDuration(0L);
            return;
        }
        LocalDateTime start = LocalDateTime.MAX;
        LocalDateTime end = LocalDateTime.MIN;
        long duration = 0L;
        for (int id : subs) {
            final Subtask subtask = subTasks.get(id);
            final LocalDateTime startTime = subtask.getStartTime();
            final LocalDateTime endTime = subtask.getEndTime();
            if(startTime == null || endTime == null)
            {
                continue;
            }

            if (startTime.isBefore(start)) {
                start = startTime;
            }
            if (endTime.isAfter(end)) {
                end = endTime;
            }
            duration += subtask.getDuration();
        }

        if(LocalDateTime.MAX.isEqual(start) || LocalDateTime.MIN.isEqual(end))
        {
            return;
        }

        epic.setDuration(duration);
        epic.setStartTime(start);
        epic.setEndTime(end);
    }
    protected void updateEpic(int epicId) {
        Epic epic = epics.get(epicId);
        checkStatusEpic(epicId);
        updateEpicDuration(epic);
    }

    protected void addToPrioritizedSet(Task task) {
        final LocalDateTime startTime = task.getStartTime();
        final LocalDateTime endTime = task.getEndTime();
        for (Task t : prioritizedTasks) {
            final LocalDateTime existStart = t.getStartTime();
            final LocalDateTime existEnd = t.getEndTime();
            if (existStart != null) {
                if (!endTime.isAfter(existStart)) {
                    continue;
                }
                if (!existEnd.isAfter(startTime)) {
                    continue;
                }
                throw new TaskValidationException("Задача пересекаются с id=" + t.getId() + " c " + existStart + " по " + existEnd);
            }
        }

        prioritizedTasks.add(task);
    }

    private void delete(Task task) {
        prioritizedTasks.remove(task);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        List <Subtask> subtasksEpic = new ArrayList<>();
        for (Integer idSubtask : getSubTaskByEpicId(id)){
            subtasksEpic.add(getSubtask(idSubtask));
        }
        return subtasksEpic;
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

        if (inProgress > 0 || done > 0 && done < epic.getSubtaskIds().size()) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else if (done > 0 && done == epic.getSubtaskIds().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }

}


