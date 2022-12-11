import manager.InMemoryTaskManager;
import manager.Manager;
import manager.Managers;
import tasks.*;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager im = new InMemoryTaskManager();



        System.out.println("начало");
        Task task1 = new Task("T1asdf", "asfas", TaskStatus.NEW);
        Task task2 = new Task("T2as1111df", "as1111fas", TaskStatus.NEW);

        Epic epic1 = new Epic("EP1as1111df", "as1111fas", TaskStatus.NEW);
        Subtask sub1 = new Subtask("SUB1as1111df", "as1111fas", TaskStatus.NEW, 1);
        Subtask sub2 = new Subtask("SUB2as1111df", "as1111fas", TaskStatus.NEW,1);


        Epic epic2 = new Epic("EP2a1333333333f", "as13333333333s", TaskStatus.NEW);
        Subtask sub3 = new Subtask("SUB3as1111df", "as1111fas", TaskStatus.NEW, 2);

        im.addTask(task1);
        im.addTask(task2);
        im.addEpic(epic1);
        im.addEpic(epic2);
        im.addSubTask(sub1);
        im.addSubTask(sub2);
        im.addSubTask(sub3);
        im.getEpic(2);
        im.getEpic(1);
        im.getTask(4);
    }
}
