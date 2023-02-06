package manager;

import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskTaskManager  {
    private final File file;

    public File getFile() {
        return file;
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }
         @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic){
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(Subtask subtask){
        super.addSubTask(subtask);
        save();
    }

    @Override
    public void clearAllTask(){
        super.clearAllTask();
        save();
    }

    @Override
    public void clearAllEpic(){
        super.clearAllEpic();
        save();
    }

    @Override
    public void clearAllSubtask(){
        super.clearAllSubtask();
        save();
    }

    @Override
    public Task getTask(int number){
        Task task = super.getTask(number);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int number){
        Epic epic = super.getEpic(number);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int number){
        Subtask subtask = super.getSubtask(number);
        save();
        return subtask;
    }

    @Override
    public void removeTask(Integer id){
        super.removeTask(id);
        save();
    }

    @Override
    public List<Epic> getEpicList() {
        return super.getEpicList();
    }

    @Override
    public List<Task> getTaskList() {
        return super.getTaskList();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public void removeEpic(Integer id){
        super.removeEpic(id);
        save();
    }


    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public void removeSubtask(Integer id){
        super.removeSubtask(id);
        save();
    }

    //  @Override
   // public List<Integer> getSubTask(int epicId) {
    //    return super.getSubTask(epicId);
  //  }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(CSVTaskFormat.getHeader());
            writer.newLine();
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                final Task task = entry.getValue();
                writer.write(CSVTaskFormat.toString(task));
                writer.newLine();
            }
            for (Map.Entry<Integer, Subtask> entry : subTasks.entrySet()) {
                final Task task = entry.getValue();
                writer.write(CSVTaskFormat.toString(task));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                final Task task = entry.getValue();
                writer.write(CSVTaskFormat.toString(task));
                writer.newLine();
            }
            writer.newLine();
            writer.write(CSVTaskFormat.historyToString(getInMemoryHistoryManager()));
            writer.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file: " + file.getName(), e);
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager taskManager = new FileBackedTasksManager(file);
        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split(System.lineSeparator());
            int generatorId = 0;
            List<Integer> history = Collections.emptyList();
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isEmpty()) {
                    history = CSVTaskFormat.historyFromString(lines[i + 1]);
                    break;
                }
                final Task task = CSVTaskFormat.fromString(line);
                final int id = task.getId();
                if (id > generatorId) {
                    generatorId = id;
                }
                taskManager.addAnyTask(task);
            }
            for (Map.Entry<Integer, Subtask> e : taskManager.subTasks.entrySet()) {
                final Subtask subtask = e.getValue();
                final Epic epic = taskManager.epics.get(subtask.getEpicId());
                epic.addSubtaskId(subtask.getId());
            }
            for (Integer taskId : history) {
                taskManager.inMemoryHistoryManager.add(taskManager.findTask(taskId));
            }
            taskManager.countId = generatorId;
        } catch (IOException e) {
            throw new ManagerSaveException("Can't read form file: " + file.getName(), e);
        }
        return taskManager;
    }

    protected void addAnyTask(Task task) {
        final int id = task.getId();
        switch (task.getType()) {
            case TASK:
                tasks.put(id, task);
                break;
            case SUBTASK:
                subTasks.put(id, (Subtask) task);
                break;
            case EPIC:
                epics.put(id, (Epic) task);
                break;
        }
    }
    protected Task findTask(Integer id) {
        final Task task = tasks.get(id);
        if (task != null) {
            return task;
        }
        final Subtask subtask = subTasks.get(id);
        if (subtask != null) {
            return subtask;
        }
        return epics.get(id);
    }
    }

