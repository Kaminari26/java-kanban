package tasks;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskTaskManagerTest {

    TaskManager manager = Managers.getDefault();

    @Test
    void checkNullEpicOnSubtask() {
        Subtask sub2 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, null);
        final NullPointerException exception = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.addSubTask(sub2);
            }
        });
        assertEquals("Подзадача - не самостоятельная задача", exception.getMessage());
    }

    @Test
    void addTaskTest() {
        Task task1 = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
        manager.addTask(task1);
        assertEquals(task1, manager.getTask(1), "Задачи не совпали");
        assertEquals(1, manager.getTaskList().size(), "Неверное количество задач");
    }

    @Test
    void addTask_IncorrectTypeForTask_NotAddTask() {
        Task task1 = new Task("T1", TypeTask.SUBTASK, "asfas", TaskStatus.NEW);

        manager.addTask(task1);

        assertEquals(0, manager.getTaskList().size(),"Неверное количество задач");
    }

@Test
    void addEpicTest() {
    Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        manager.addEpic(epic1);
        assertEquals(epic1, manager.getEpic(1), "Эпики не совпали");
        assertEquals(1, manager.getEpicList().size(), "Неверное количество эпиков");
    }

    @Test
    void addSubTaskTest() {
        Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        Subtask sub2 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, 1);
        Subtask sub3 = new Subtask("SUB3", TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, 1);
        manager.addEpic(epic1);
        manager.addSubTask(sub2);
        assertEquals("[2]",manager.getEpic(1).getSubtaskIds().toString(),"Проблема с id у сабтаска");
        manager.addSubTask(sub3);
        assertEquals(2, manager.getSubtaskList().size(),"Проблема с количеством сабтасков");
    }

    @Test
    void getEpicListTest(){
        Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        Epic epic2 = new Epic("EP2",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        assertEquals(0, manager.getEpicList().size(),"Количество эпиков в листе не совпало");
        manager.addEpic(epic1);
        assertEquals(1, manager.getEpicList().size(),"Количество эпиков в листе не совпало");
        manager.addEpic(epic2);
        assertEquals(2, manager.getEpicList().size(),"Количество эпиков в листе не совпало");
    }

    @Test
    void getTaskListTest(){
    Task task1 = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
    Task task2 = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
    assertEquals(0,manager.getTaskList().size(),"Неверное количество элементов в списке");
    manager.addTask(task1);
    assertEquals(1, manager.getTaskList().size(),"Неверное количество элементов в списке");
    manager.addTask(task2);
    assertEquals(2, manager.getTaskList().size(),"Неверное количество элементов в списке");
        }

        @Test
        void getSubtaskListNormal(){
            Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
            Subtask sub1 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, 1);
            Subtask sub2 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, 1);
            assertEquals(0, manager.getSubtaskList().size(),"Неверное количество элементов в списке");
            manager.addEpic(epic1);
            manager.addSubTask(sub1);
            assertEquals(1, manager.getSubtaskList().size(),"Неверное количество элементов в списке");
            manager.addSubTask(sub2);
            assertEquals(2, manager.getSubtaskList().size(),"Неверное количество элементов в списке");
        }
        @Test
    void clearAllTaskTest(){
            Task task1 = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
            Task task2 = new Task("T2", TypeTask.TASK, "asfas", TaskStatus.NEW);
            Task task3 = new Task("T3", TypeTask.TASK, "asfas", TaskStatus.NEW);
            manager.addTask(task1);
            manager.addTask(task2);
            manager.addTask(task3);
            manager.clearAllTask();
            assertEquals(0, manager.getTaskList().size());
        }

        @Test
    void clearAllEpicTest(){
            Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
            Epic epic2 = new Epic("EP2",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
            manager.addEpic(epic1);
            manager.addEpic(epic2);
            manager.clearAllEpic();
            assertEquals(0, manager.getEpicList().size());
        }
        @Test
    void clearAllSubTaskTest(){
            Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
            Epic epic2 = new Epic("EP2",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
            Subtask sub1 = new Subtask("SUB1", TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW, 1);
            Subtask sub2 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW, 1);
            Subtask sub3 = new Subtask("SUB3", TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW, 2);
            manager.addEpic(epic1);
            manager.addEpic(epic2);
            manager.addSubTask(sub1);
            manager.addSubTask(sub2);
            manager.addSubTask(sub3);
            manager.clearAllSubtask();
            assertEquals(0, manager.getSubtaskList().size());
        }
        @Test
    void getTaskTest(){
            Task task1 = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
            manager.addTask(task1);
            assertNull(manager.getTask(2));
            assertEquals(task1, manager.getTask(1),"Нужная задача не была найдена");
        }
        @Test
    void getEpicTest(){
            Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
            manager.addEpic(epic1);
            assertNull(manager.getEpic(2),"Был вызван несуществующий эпик");
            assertEquals(epic1, manager.getEpic(1),"Нужный эпик не найден");
        }
    @Test
    void getSubtaskTest(){
        Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        Subtask sub2 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, 1);
        manager.addEpic(epic1);
        manager.addSubTask(sub2);
        assertEquals(sub2, manager.getSubtask(2),"Нужный сабтаск не найден");
        assertNull(manager.getSubtask(3),"Найден несуществующий сабтаск");

    }
    @Test
    void updateTaskTest(){
        Task task1 = new Task("T1", TypeTask.TASK, "До теста", TaskStatus.NEW);
        Task task2 = new Task("T1", TypeTask.TASK, "После обновы", TaskStatus.NEW);
        manager.addTask(task1);
        task2.setId(task1.id);
        manager.updateTask(task2);
        assertEquals(task2, manager.getTask(1));

    }
    @Test
        void removeTaskTest(){
        Task task1 = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
        manager.addTask(task1);
        manager.getTask(1);
        assertEquals("[tasks.Task{name='T1', description='asfas', id=1, status=NEW}]",
                manager.getHistory().toString());
        manager.removeTask(1);
        assertNull(manager.getTask(1),"Задача не была удалена");
        assertEquals(0, manager.getHistory().size(),"размер истории не сошелся");
    }
    @Test
        void removeEpicTest(){
        Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        manager.addEpic(epic1);
        manager.getEpic(1);
        assertEquals("[tasks.Epic{subList=[], name='EP1', description='as1111fas', id=1, status=NEW}]",
                manager.getHistory().toString());
        manager.removeEpic(1);
        assertNull(manager.getEpic(1),"Задача не была удалена");
        assertEquals(0, manager.getHistory().size(),"размер истории не сошелся");
    }

    @Test
        void removeSubtaskTest(){ //--
        Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        Subtask sub2 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW, 1);
        manager.addEpic(epic1);
        manager.addSubTask(sub2);
        assertEquals(sub2,manager.getSubtask(2));

        manager.removeSubtask(2);

        assertNull(manager.getSubtask(2));
    }
    @Test
        void getSubTask(){
        Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        Subtask sub1 = new Subtask("SUB1", TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW, 1);
        Subtask sub2 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW, 1);
        manager.addEpic(epic1);
        assertEquals(0,manager.getSubTask(1).size(),"Неверное количество сабтасков у эпика");
        manager.addSubTask(sub1);
        manager.addSubTask(sub2);
        assertEquals(2,manager.getSubTask(1).size(),"Неверное количество сабтасков у эпика");
    }

}