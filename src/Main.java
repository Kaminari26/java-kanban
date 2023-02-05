import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import tasks.*;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        TaskManager tasksManager = Managers.getDefault();

        Task task1 = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
        Task task4 = new Task("T4",TypeTask.TASK, "asf444444s", TaskStatus.NEW);
        Task task2 = new Task("T2",TypeTask.TASK, "as1111fas", TaskStatus.NEW);

        Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        Subtask sub1 = new Subtask("SUB",TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW, 4);
        Subtask sub2 = new Subtask("SUB2",TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW,3);
        Subtask sub3 = new Subtask("SUB3",TypeTask.SUBTASK, "33333333", TaskStatus.NEW,3);

        Epic epic2 = new Epic("EP2", TypeTask.SUBTASK,"as13333333333s", TaskStatus.NEW);
        tasksManager.addTask(task1);
        tasksManager.addTask(task4);
        tasksManager.addTask(task2);
        tasksManager.addEpic(epic1);
        tasksManager.getTask(1);
        tasksManager.getTask(2);
        tasksManager.getTask(3);
        tasksManager.addSubTask(sub1);
        System.out.println("history: " + tasksManager.getHistory());
        System.out.println("Tasks: " + tasksManager.getTaskList());

        File file = new File("./resources/task.csv");
        FileBackedTasksManager managerFromFile = FileBackedTasksManager.loadFromFile(file);
        System.out.println("history : " + managerFromFile.getHistory());
        System.out.println("Tasks: " + managerFromFile.getTaskList());
    }
}
