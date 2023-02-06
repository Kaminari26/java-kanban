package manager;

import tasks.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskTaskManager implements TaskManager {

    protected final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subTasks = new HashMap<>();
    protected Integer countId = 0;




    @Override
    public void addTask(Task task) {
        try {
            if (task.getType().equals(TypeTask.TASK)) {
                countId++;
                task.setId(countId);
                tasks.put(countId, task);
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addEpic(Epic epic) {
        try {
            if (epic.getType().equals(TypeTask.EPIC)) {
                countId++;
                epic.setId(countId);
                epics.put(countId, epic);
            }
        } catch(NullPointerException e) {
            System.out.println(e.getMessage());
        }
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
    }

    @Override
    public void removeTask(Integer id) {
        inMemoryHistoryManager.remove(id);
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
    public void removeSubtask(Integer id) {
        Integer epicId = subTasks.get(id).getEpicId();
        epics.get(epicId).removeSubtask(id);
        subTasks.remove(id);
        inMemoryHistoryManager.remove(id);
        checkStatusEpic(epicId);
    }

 //   @Override
  //  public List<Integer> getSubTask(int epicId) {
  //      return List.copyOf(epics.get(epicId).getSubtaskIds());
  //  }

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
            if (startTime.isBefore(start)) {
                start = startTime;
            }
            if (endTime.isAfter(end)) {
                end = endTime;
            }
            duration += subtask.getDuration();
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
    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        TreeSet<Task> treeTasksSortTime = new TreeSet<>();
        if (!tasks.isEmpty()) {
            treeTasksSortTime.addAll(tasks.values());
        }
        if (!epics.isEmpty()){
            treeTasksSortTime.addAll(epics.values());
        }
        if (!subTasks.isEmpty()) {
            treeTasksSortTime.addAll(subTasks.values());
        }
        return new ArrayList<>(treeTasksSortTime);
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


