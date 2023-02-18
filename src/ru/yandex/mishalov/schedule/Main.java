package ru.yandex.mishalov.schedule;

import ru.yandex.mishalov.schedule.server.HttpTaskServer;
import ru.yandex.mishalov.schedule.server.KVServer;
import ru.yandex.mishalov.schedule.manager.FileBackedTasksManager;
import ru.yandex.mishalov.schedule.manager.HttpTaskManager;
import ru.yandex.mishalov.schedule.manager.TaskManager;
import ru.yandex.mishalov.schedule.tasks.*;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        TaskManager tasksManager = new FileBackedTasksManager(new File("resources/task.csv"));

        Task task1 = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
        Task task4 = new Task("T4", TypeTask.TASK, "asf444444s", TaskStatus.NEW);
        Task task2 = new Task("T2", TypeTask.TASK, "as1111fas", TaskStatus.NEW);
        Epic epic1 = new Epic("EP1", TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        Subtask sub1 = new Subtask("SUB", TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW, 5);
        Subtask sub2 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW,5);
        Subtask sub3 = new Subtask("SUB3", TypeTask.SUBTASK, "33333333", TaskStatus.NEW,5);
        Epic epic2 = new Epic("EP2", TypeTask.EPIC, "as13333333333s", TaskStatus.NEW);
        tasksManager.addTask(task1);
        tasksManager.addTask(task4);
        tasksManager.addTask(task2);
        tasksManager.addEpic(epic1);
        tasksManager.getTask(1);
        tasksManager.getTask(2);
        tasksManager.getTask(3);
        tasksManager.addEpic(epic2);
        tasksManager.addSubTask(sub1);
        tasksManager.addSubTask(sub2);

        tasksManager.addSubTask(sub3);

        System.out.println(tasksManager.getHistory());
        System.out.println(tasksManager.getSubtaskList());
        System.out.println(tasksManager.getEpicList());
        tasksManager.removeSubtask(6);
        System.out.println(tasksManager.getSubtaskList());
        KVServer kvServer = new KVServer();
        kvServer.start();
        TaskManager httpManager = new HttpTaskManager("http://localhost:8078");
        HttpTaskServer taskServer = new HttpTaskServer(httpManager);
        httpManager.addTask(task1);
        httpManager.addTask(task1);
    }
}
