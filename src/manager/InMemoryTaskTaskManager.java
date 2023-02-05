package manager;

import tasks.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskTaskManager implements TaskManager {

    protected final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subTasks = new HashMap<>();
    protected Integer countId = 0;


    @Override
    public void addTask(Task task){
        try {
            if (task.getType().equals(TypeTask.TASK)) {
                countId++;
                task.setId(countId);
                tasks.put(countId, task);
            }
        }catch(Exception e){
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
        } catch(NullPointerException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addSubTask(Subtask subtask){
        if (subtask.getType().equals(TypeTask.SUBTASK)) {
            if (epics.containsKey(subtask.getEpicId())) {
                try {
                    countId++;
                    subtask.setId(countId);
                    subTasks.put(countId, subtask);
                    epics.get(subtask.getEpicId()).addSubtaskId(countId);
                    checkStatusEpic(subtask.getEpicId());
                } catch (NullPointerException e) {
                    System.out.println(e.getMessage());
                }


            } else {
                throw new NullPointerException("Подзадача - не самостоятельная задача");
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
    public void clearAllEpic(){
        clearAllSubtask();
        epics.clear();
    }

    @Override
    public void clearAllSubtask(){
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
    public Epic getEpic(int number){
        inMemoryHistoryManager.add(epics.get(number));
        return epics.get(number);
    }

    @Override
    public Subtask getSubtask(int number){
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
    public void removeTask(Integer id){ //+
        inMemoryHistoryManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void removeEpic(Integer id){ //+
        for (Integer subTaskId : epics.get(id).getSubtaskIds()) {
            subTasks.remove(subTaskId);
            inMemoryHistoryManager.remove(subTaskId);
        }

        epics.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void removeSubtask(Integer id){ //-
        Integer epicId = subTasks.get(id).getEpicId();
        epics.get(epicId).removeSubtask(id);
        subTasks.remove(id);
        inMemoryHistoryManager.remove(id);
        checkStatusEpic(epicId);
    }

    @Override
    public List<Integer> getSubTask(int epicId) {
        return List.copyOf(epics.get(epicId).getSubtaskIds());
    }

    @Override

    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
    @Override
    public LocalDateTime craftLocalDate(String string){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("DD.MM.yyyy HH:mm");
        return LocalDateTime.parse(string,inputFormatter);
    }

    public void setEpicStartTime(Epic epic){
        LocalDateTime localDateTime = null;
        for(Integer id : epic.getSubtaskIds()){
            if(getSubtask(id).getStartTime() != null && localDateTime == null){
            localDateTime = getSubtask(id).getStartTime();
            }else if (getSubtask(id).getStartTime() != null && localDateTime.isAfter(getSubtask(id).getStartTime())){
                localDateTime = getSubtask(id).getStartTime();
            }
        }
        epic.setStartTime(localDateTime);
    }

    public void setEndTimeEpic(Epic epic){
        LocalDateTime localDateTime = null;
        for(Integer id : epic.getSubtaskIds()){
            if(getSubtask(id).getStartTime() != null && localDateTime == null){
                localDateTime = getSubtask(id).getStartTime();
            }else if (getSubtask(id).getStartTime() != null && localDateTime.isBefore(getSubtask(id).getStartTime())){
                localDateTime = getSubtask(id).getStartTime();
            }
        }
        epic.setEndTime(localDateTime);
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


