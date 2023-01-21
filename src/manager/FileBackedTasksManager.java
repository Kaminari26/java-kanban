package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.*;
import java.nio.file.Files;
import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskTaskManager  {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private static class CSVTaskFormat {
        public static String getHeader() {
            return "id,type,name,status,description,epic";
        }
        private static String getAllTasksInString(List<Task> taskList) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Task task : taskList) {
                stringBuilder.append(toString(task));
                stringBuilder.append(System.lineSeparator());
            }
            return stringBuilder.toString();
        }

        private static String toString(Task task) {
            String str = String.join(",", Integer.toString(task.getId()),
                    task.getType().name(),
                    task.getName(),
                    task.getStatus().toString(),
                    task.getDescription(),
                    (task.getType().equals(TypeTask.SUBTASK) ? ((Subtask)task).getEpicId().toString() : ""));
            return str;
        }

        private static Task fromString(String value){
            String[] splitString = value.split(",");
            TypeTask typeTask = TypeTask.valueOf(splitString[1]);
            switch (typeTask) {
                case TASK:
                    return new Task(
                            Integer.parseInt(splitString[0]),
                            typeTask,
                            splitString[2],
                            TaskStatus.valueOf(splitString[3]),
                            splitString[4] != null ? splitString[4] : "");
                case SUBTASK:
                    return new Subtask(Integer.parseInt(splitString[0]),
                            splitString[2],
                            typeTask,
                            splitString[4] != null ? splitString[4] : "",
                            TaskStatus.valueOf(splitString[3]),
                            Integer.parseInt(splitString[5]));
                case EPIC:
                    return new Epic(Integer.parseInt(splitString[0]),
                            splitString[2],
                            typeTask,
                            splitString[4] != null ? splitString[4] : "",
                            TaskStatus.valueOf(splitString[3]));
            }

            return null;
        }

        public static String historyToString(HistoryManager historyManager) {
            final List<Task> history = historyManager.getHistory();
            if (history.isEmpty()) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(history.get(0).getId());
            for (int i = 1; i < history.size(); i++) {
                Task task = history.get(i);
                sb.append(",");
                sb.append(task.getId());
            }
            return sb.toString();
        }

        static List<Integer> historyFromString(String value) {
            String[] splitStringHistory = value.split(",");
            List<Integer> list = new ArrayList<>();
            for (String story : splitStringHistory) {
                list.add((Integer.parseInt(story)));
            }
            return list;
        }
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

    @Override
    public List<Integer> getSubTask(int epicId) {
        return super.getSubTask(epicId);
    }

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

    public static void main(String[] args) {
        File file = new File("./resources/fileWriter.CSV");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        Task task1 = new Task("T1",TypeTask.TASK, "asfas", TaskStatus.NEW);
        Task task4 = new Task("T4",TypeTask.TASK, "asf444444s", TaskStatus.NEW);
        Task task2 = new Task("T2",TypeTask.TASK, "as1111fas", TaskStatus.NEW);

        Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        Subtask sub1 = new Subtask("SUB",TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW, 4);
        Subtask sub2 = new Subtask("SUB2",TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW,3);
        Subtask sub3 = new Subtask("SUB3",TypeTask.SUBTASK, "33333333", TaskStatus.NEW,3);
        fileBackedTasksManager.loadFromFile(file);
        Epic epic2 = new Epic("EP2", TypeTask.SUBTASK,"as13333333333s", TaskStatus.NEW);
        fileBackedTasksManager.addTask(task1);
        fileBackedTasksManager.addTask(task4);
        fileBackedTasksManager.addTask(task2);
        fileBackedTasksManager.addEpic(epic1);
        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getTask(2);
        fileBackedTasksManager.getTask(3);
        fileBackedTasksManager.addSubTask(sub1);
        System.out.println("history: " + fileBackedTasksManager.getHistory());
        System.out.println("Tasks: " + fileBackedTasksManager.getTaskList());
    }
    }

