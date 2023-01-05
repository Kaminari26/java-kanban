import manager.*;
import tasks.*;

public class Main {
    static TaskManager inMemoryTaskManager = Managers.getDefault();
    public static void main(String[] args) {
        System.out.println("начало");
        Task task1 = new Task("T1", "asfas", TaskStatus.NEW);
        Task task2 = new Task("T2", "as1111fas", TaskStatus.NEW);

        Epic epic1 = new Epic("EP1", "as1111fas", TaskStatus.NEW);
        Subtask sub1 = new Subtask("SUB", "as1111fas", TaskStatus.NEW, 3);
        Subtask sub2 = new Subtask("SUB2", "as1111fas", TaskStatus.NEW,3);
        Subtask sub3 = new Subtask("SUB3", "33333333", TaskStatus.NEW,3);


        Epic epic2 = new Epic("EP2", "as13333333333s", TaskStatus.NEW);

        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addEpic(epic2);
        inMemoryTaskManager.addSubTask(sub1);
        inMemoryTaskManager.addSubTask(sub2);
        inMemoryTaskManager.addSubTask(sub3);
        inMemoryTaskManager.getTask(2);
        System.out.println( "история: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getTask(2);
        System.out.println( "история: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getEpic(3);
        System.out.println( "история: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubTask(3);
        System.out.println( "история: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubTask(3);

        System.out.println( "история:  тут еще саб" + inMemoryTaskManager.getSubTask(3));
        System.out.println( "история:  тут еще саб" + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.removeEpic(3);
        System.out.println( "история: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getTask(2);
        System.out.println( "история: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.removeTask(2);
        System.out.println( "история: " + inMemoryTaskManager.getHistory());




    }
}
