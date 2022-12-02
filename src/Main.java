import manager.Manager;
import tasks.*;

public class Main {
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
        System.out.println(manager.epicAndCo(2));


//  я не понял из задачи, нужно ли подтерать мои тесты, оставил как есть
    }
}
