
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    final String epicClassName = "Epic";
    final String taskClassName = "Task";
    final String subTaskClassName = "Subtask";

    Integer count = 0;
    HashMap<Integer, Task> taskHashMap = new HashMap<>();
    HashMap<String, List<Integer>> mapForMethod = new HashMap<>();

    public static void main(String[] args) {
        Manager manager = new Manager();
        System.out.println("начало");
        Task task1 = new Task("T1asdf", "asfas", TaskStatus.NEW);
        Task task2 = new Task("T2as1111df", "as1111fas", TaskStatus.NEW);

        Epic epic1 = new Epic("EP1as1111df", "as1111fas", TaskStatus.NEW);
        Subtask sub1 = new Subtask("SUB1as1111df", "as1111fas", TaskStatus.NEW, epic1);
        Subtask sub2 = new Subtask("SUB2as1111df", "as1111fas", TaskStatus.NEW,epic1);


        Epic epic2 = new Epic("EP2a1333333333f", "as13333333333s", TaskStatus.NEW);
        Subtask sub3 = new Subtask("SUB3as1111df", "as1111fas", TaskStatus.NEW, epic2);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSubask(sub1, epic1);
        manager.addSubask(sub2, epic1);
        manager.addSubask(sub3, epic2);
        ;
        System.out.println(manager.epicAndCo(2));


//  я не понял из задачи, нужно ли подтерать мои тесты, оставил как есть

    }


    public void addTask(Task task) {
        task.id = count;
        taskHashMap.put(count, task);
        fillMap(taskClassName,task.id);
        count++;
    }

    public void addEpic(Epic epic) {
        epic.id = count;
        taskHashMap.put(count, epic);
        fillMap(epicClassName,epic.id);
        count++;
    }

    public void addSubask(Subtask subtask, Epic epic) {
        subtask.id = count;
        taskHashMap.put(count, subtask);
        epic.subList.add(subtask);
        fillMap(subTaskClassName,subtask.id);
        count++;
    }

    public List<Epic> epicList() {
        ArrayList<Epic> eTask = new ArrayList<>();

        for (Integer id : mapForMethod.get(epicClassName)) {
            eTask.add((Epic) taskHashMap.get(id));
        }
        return eTask;
    }
    public List<Subtask> subtaskList() {
        ArrayList<Subtask> sTusk = new ArrayList<>();

        for (Integer id : mapForMethod.get(subTaskClassName)) {
            sTusk.add((Subtask) taskHashMap.get(id));
        }
        return sTusk;
    }
    public List<Task> taskList() {
        ArrayList<Task> aTusk = new ArrayList<>();

        for (Integer id : mapForMethod.get(taskClassName)) {
            aTusk.add(taskHashMap.get(id));
        }
        return aTusk;
    }

    public void clearAllTask() {
        for (Integer id : mapForMethod.get(taskClassName)) {
            taskHashMap.remove(id);
        }
        mapForMethod.remove(taskClassName);
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

    public Task getTusk(int number) {

        return taskHashMap.get(number);
    }
    public Epic getEpic(int number) {

        return (Epic) taskHashMap.get(number);
    }
    public Subtask getSubtusk(int number) {

        return (Subtask) taskHashMap.get(number);
    }

    public void updateTask(Task task) {

        taskHashMap.put(task.id, task);

    }
    public void updateEpic(Epic epic) {
        taskHashMap.put(epic.id, epic);
        checkStatusEpic(epic);
    }
    public void updateSubtask(Subtask subtask) {

        taskHashMap.put(subtask.id, subtask);

        checkStatusEpic(subtask.epic);
    }

    public void removeTask(Integer number) {
        taskHashMap.remove(number);
        mapForMethod.get(taskClassName).remove(number);
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

        return ((Epic)taskHashMap.get(id)).subList;
    }

    private void fillMap(String type, Integer id) {

        if (mapForMethod.containsKey(type)) {
            List<Integer> list = mapForMethod.get(type);
            list.add(id);
        } else {
            List<Integer> list = new ArrayList<>();
            list.add(id);
            mapForMethod.put(type, list);
        }


    }
    public void checkStatusEpic(Epic epic){
        int done = 0;
        int inProgress = 0;
        for( Subtask list : epic.subList ){
            if (list.status == TaskStatus.DONE){
                done = done + 1;
            }else if (list.status == TaskStatus.IN_PROGRESS){
                inProgress = inProgress + 1;
            }
        }
        if (inProgress < 0){
            epic.status = TaskStatus.IN_PROGRESS;
        }
        else if (done == epic.subList.size()){
            epic.status = TaskStatus.DONE;
        }else{
            epic.status = TaskStatus.NEW;
        }
    }
}


