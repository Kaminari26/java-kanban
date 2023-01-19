package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.*;
import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskTaskManager  {

    public static void main(String[] args) throws IOException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        Task task1 = new Task("T1",TypeTask.TASK, "asfas", TaskStatus.NEW);
        Task task4 = new Task("T4",TypeTask.TASK, "asf444444s", TaskStatus.NEW);
        Task task2 = new Task("T2",TypeTask.TASK, "as1111fas", TaskStatus.NEW);

        Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        Subtask sub1 = new Subtask("SUB",TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW, 4);
        Subtask sub2 = new Subtask("SUB2",TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW,3);
        Subtask sub3 = new Subtask("SUB3",TypeTask.SUBTASK, "33333333", TaskStatus.NEW,3);
        File file = new File("./resources/fileWriter.CSV");
        fileBackedTasksManager.loadFromFile(file);
        System.out.println("Tasks Do" + fileBackedTasksManager.getTaskList());
        System.out.println("Istoria Do" + fileBackedTasksManager.getHistory());

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

    @Override
    public void addTask(Task task){
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
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
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

    private void save() {
        try {
            Writer fileWriter = new FileWriter("./resources/fileWriter.CSV");
            List<Task> sortedTaskList = new ArrayList<>();
            sortedTaskList.addAll(getEpicList());
            sortedTaskList.addAll(getTaskList());
            sortedTaskList.addAll(getSubtaskList());
            Collections.sort(sortedTaskList, new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    if(o1.getId()<o2.getId())
                    {
                        return -1;
                    }else if(o1.getId()>o2.getId()){
                        return 1;
                    }

                    return 0;
                }
            });
            fileWriter.write(getAllTasksInString(sortedTaskList));

            fileWriter.write(historyToString(getInMemoryHistoryManager()));
            fileWriter.close();
        }catch (IOException e){
            ManagerSaveException managerSaveException = new ManagerSaveException("Ошибка");
            System.out.println(managerSaveException.getMessage());
        }

    }

    private String getAllTasksInString(List<Task> taskList)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : taskList) {
            stringBuilder.append(toString(task));
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
   private static String toString(Task task){
        String str = String.join(",", Integer.toString(task.getId()),
                task.getType().name(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                (task.getType().equals(TypeTask.SUBTASK) ? ((Subtask)task).getEpicId().toString() : ""));
    return str;
   }

   private static Task fromString(String value){
       String[] split = value.split(",");
       TypeTask typeTask = TypeTask.valueOf(split[1]);
       switch (typeTask)
       {
           case TASK:
               return new Task(
                       Integer.parseInt(split[0]),
                       typeTask,
                       split[2],
                       TaskStatus.valueOf(split[3]),
                       split[4] != null ? split[4] : "");
           case SUBTASK:
               return new Subtask(Integer.parseInt(split[0]),
                       split[2],
                       typeTask,
                       split[4] != null ? split[4] : "",
                       TaskStatus.valueOf(split[3]),
                       Integer.parseInt(split[5]));
           case EPIC:
               return  new Epic(Integer.parseInt(split[0]),
                       split[2],
                       typeTask,
                       split[4] != null ? split[4] : "",
                       TaskStatus.valueOf(split[3]));
       }

       return null;
   }
    static String historyToString(HistoryManager manager){
        List<Task> list = manager.getHistory();
        String[] str = new String[list.size()];
        int hat = 0;
        for (Task token : list){
            str[hat] = Integer.toString(token.getId()) ;
            hat++;
        }
        String hist = String.join(",",str);

        return hist;
    }
    static List<Integer> historyFromString(String value){
        String[] split = value.split(",");
        List<Integer> list = new ArrayList<>();
        for (String story : split){
            list.add((Integer.parseInt(story)));
        }
        return list;
    }
    private void loadFromFile (File file) throws IOException {
        if(!file.exists())
        {
            return;
        }
        ArrayList <String> fileStrings = new ArrayList<>();
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        while (br.ready()){
            String line = br.readLine();
            if(!line.isBlank()) {
                fileStrings.add(line);
            }
        }
        int lastIndexOf = fileStrings.size()-1 < 0 ? 0 : fileStrings.size()-1;
        List<Integer> history = lastIndexOf == 0 ? new ArrayList<>() : historyFromString(fileStrings.get(lastIndexOf));
        if (lastIndexOf != 0)
        {
            fileStrings.remove(lastIndexOf);
        }

        for (String st : fileStrings){
            Task task = fromString(st);
            if(history.contains(task.getId()))
            {
                getInMemoryHistoryManager().add(task);
            }
            if(task.getType().equals(TypeTask.TASK)){
                super.addTask(task);
            }else if(task.getType().equals(TypeTask.EPIC)){
                super.addEpic((Epic) task);
            }else if (task.getType().equals(TypeTask.SUBTASK)){
                super.addSubTask((Subtask) task);
            }
        }


    }
    private static class ManagerSaveException extends Exception{
        public ManagerSaveException(String message) {
            super(message);
        }
    }
    }

