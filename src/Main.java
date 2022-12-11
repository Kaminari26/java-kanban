import manager.*;
import tasks.*;

public class Main {
    public static void main(String[] args) {
        ITaskManager im = new InMemoryTaskTaskManager();

        System.out.println("начало");
        Task task1 = new Task("T1", "asfas", TaskStatus.NEW);
        Task task2 = new Task("T2", "as1111fas", TaskStatus.NEW);

        Epic epic1 = new Epic("EP1", "as1111fas", TaskStatus.NEW);
        Subtask sub1 = new Subtask("SUB", "as1111fas", TaskStatus.NEW, 1);
        Subtask sub2 = new Subtask("SUB2", "as1111fas", TaskStatus.NEW,1);


        Epic epic2 = new Epic("EP2", "as13333333333s", TaskStatus.NEW);
        Subtask sub3 = new Subtask("SUB3", "as1111fas", TaskStatus.NEW, 2);

        im.addTask(task1);
        im.addTask(task2);
        im.addEpic(epic1);
        im.addEpic(epic2);
        im.addSubTask(sub1);
        im.addSubTask(sub2);
        im.addSubTask(sub3);
        im.getTask(2);
        im.getTask(2);
       im.getEpic(3);
        System.out.println( "история: " + im.getHistory());



    }
}
