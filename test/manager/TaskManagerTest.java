package manager;

import helpers.LocalDateTimeHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import tasks.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
   public T manager;
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

   public TaskManagerTest(T managers) {
        manager = managers;
    }

    protected void initTasks() {
        task = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
        taskManager.addTask(task);
        epic = new Epic("EP1", TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        taskManager.addEpic(epic);
        subtask = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, epic.getId());
        taskManager.addSubTask(subtask);
    }

    @Test
    void addTaskHappyPath() {
        initTasks();
        manager.addTask(task);
        assertEquals(task, manager.getTask(1), "Задачи не совпали");
        assertEquals(1, manager.getTaskList().size(), "Неверное количество задач");
    }

    @Test
    void addTask_IncorrectTypeForTask_NotAddTask() {
        initTasks();
        manager.addTask(task);
        assertEquals(0, manager.getTaskList().size(), "Неверное количество задач");
    }

   @Test
   void addTaskExceptionTest() {
       initTasks();
      manager.addTask(task);
      assertEquals(0, manager.getTaskList().size(), "Неверное количество задач");
   }

  @Test
  void addTaskNullTypeTest() {
      task = new Task("T1", null, "asfas", TaskStatus.NEW);
      manager.addTask(task);
      assertEquals(0, manager.getTaskList().size(), "Неверное количество задач");
  }

    @Test
    void addTaskNullTest() {
     manager.addTask(null);
     assertEquals(0, manager.getTaskList().size());
    }

    @Test
    void addEpic() {
        initTasks();
        manager.addEpic(epic);
        assertEquals(epic, manager.getEpic(1), "Эпики не совпали");
        assertEquals(1, manager.getEpicList().size(), "Неверное количество эпиков");
    }

     @Test
     void addEpicIncorrectType() {
        epic = new Epic("EP1", TypeTask.TASK, "as1111fas", TaskStatus.NEW);
        manager.addEpic(epic);
        assertEquals(0, manager.getEpicList().size(), "Неверное количество эпиков");
     }

     @Test
     void addEpicNull() {
         manager.addEpic(null);
         assertEquals(0,manager.getEpicList().size());
     }

    @Test
    void addSubTask() {
        initTasks();
        manager.addEpic(epic);
        manager.addSubTask(subtask);
        assertEquals("[2]", manager.getEpic(1).getSubtaskIds().toString(), "Проблема с id у сабтаска");
        assertEquals(1, manager.getSubtaskList().size(), "Проблема с количеством сабтасков");
    }

    @Test
    void addSubTaskWithoutEpic() {
       initTasks();
       final NullPointerException exception = assertThrows(NullPointerException.class, new Executable() {
      @Override
      public void execute() throws Throwable {
            manager.addSubTask(subtask);
      }
     });
        assertEquals("Подзадача - не самостоятельная задача", exception.getMessage());
    }
    @Test
    void getEpicList() {
         Epic epic1 = new Epic("EP1", TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
         Epic epic2 = new Epic("EP2", TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
         assertEquals(0, manager.getEpicList().size(), "Количество эпиков в листе не совпало");
         manager.addEpic(epic1);
         assertEquals(1, manager.getEpicList().size(), "Количество эпиков в листе не совпало");
         manager.addEpic(epic2);
         assertEquals(2, manager.getEpicList().size(), "Количество эпиков в листе не совпало");
    }

    @Test
    void getSubtaskList() {
         Epic epic1 = new Epic("EP1", TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
         Subtask sub1 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, 1);
         Subtask sub2 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, 1);
         manager.addEpic(epic1);
         assertEquals(0, manager.getSubtaskList().size(), "Неверное количество элементов в списке");
         manager.addSubTask(sub1);
         assertEquals(1, manager.getSubtaskList().size(), "Неверное количество элементов в списке");
         manager.addSubTask(sub2);
         assertEquals(2, manager.getSubtaskList().size(), "Неверное количество элементов в списке");
    }

    @Test
    void getTaskList() {
         Task task1 = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
         Task task2 = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
         assertEquals(0, manager.getTaskList().size(), "Неверное количество элементов в списке");
         manager.addTask(task1);
         assertEquals(1, manager.getTaskList().size(), "Неверное количество элементов в списке");
         manager.addTask(task2);
         assertEquals(2, manager.getTaskList().size(), "Неверное количество элементов в списке");
    }

    @Test
    void clearAllTask() {
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
    void clearAllEpic() {
         Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
         Epic epic2 = new Epic("EP2",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
         manager.addEpic(epic1);
         manager.addEpic(epic2);
         manager.clearAllEpic();
         assertEquals(0, manager.getEpicList().size());
    }

    @Test
    void clearAllSubtask() {
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
    void getTask() {
       initTasks();
       manager.addTask(task);
       assertNull(manager.getTask(2));
       assertEquals(task, manager.getTask(1), "Нужная задача не была найдена");
    }

    @Test
    void getEpic() {
       initTasks();
       manager.addEpic(epic);
       assertNull(manager.getEpic(2));
       assertEquals(epic, manager.getEpic(1), "Нужная задача не была найдена");
    }

    @Test
    void getSubtask() {
       initTasks();
       manager.addEpic(epic);
       manager.addSubTask(subtask);
       assertEquals(subtask, manager.getSubtask(2),"Нужный сабтаск не найден");
       assertNull(manager.getSubtask(3),"Найден несуществующий сабтаск");
    }

    @Test
    void updateTask() {
       Task task1 = new Task("T1", TypeTask.TASK, "as1111fas", TaskStatus.NEW);
       Task task2 = new Task("T1", TypeTask.TASK, "as1111fas", TaskStatus.NEW);
       manager.addTask(task1);
       task2.setId(task1.getId());
       manager.updateTask(task2);
       assertEquals(task2, manager.getTask(1),"Не удалось обновить таск");
    }

    @Test
    void updateEpic() {
       Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
       Epic epic2 = new Epic("EP2",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
       manager.addEpic(epic1);
       epic2.setId(epic1.getId());
       manager.updateEpic(epic2);
       assertEquals(epic2,manager.getEpic(1),"Не удалось обновить Эпик");
    }

    @Test
    void updateSubtask() {
       Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
       Subtask sub1 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, 1);
       Subtask sub2 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, 1);
       manager.addEpic(epic1);
       manager.addSubTask(sub1);
       sub2.setId(sub1.getId());
       manager.updateSubtask(sub2);
       assertEquals(sub2,manager.getSubtask(2), "Не удалось обновить Сабтаск");
    }

    @Test
    void removeTask() {
        initTasks();
        manager.addTask(task);
        manager.getTask(1);
        manager.removeTask(1);
        assertNull(manager.getTask(1),"Задача не была удалена");
        assertEquals(0, manager.getHistory().size(), "размер истории не сошелся");
    }

    @Test
    void removeEpic() {
       initTasks();
       manager.addEpic(epic);
       manager.getEpic(1);
       assertEquals("[tasks.Epic{subList=[], name='EP1', description='as1111fas', id=1, status=NEW}]",
               manager.getHistory().toString());
       manager.removeEpic(1);
       assertNull(manager.getEpic(1),"Задача не была удалена");
       assertEquals(0, manager.getHistory().size(),"размер истории не сошелся");
    }

    @Test
    void removeSubtask() {
       initTasks();
       manager.addEpic(epic);
       manager.addSubTask(subtask);
       assertEquals(subtask,manager.getSubtask(2));

       manager.removeSubtask(2);

       assertNull(manager.getSubtask(2));
    }

    @Test
    void getHistory() {
       initTasks();
       manager.addTask(task);
       manager.addEpic(epic);
       assertEquals(0,manager.getHistory().size());
       manager.getTask(2);
       assertEquals(0,manager.getHistory().size());
       manager.getEpic(1);
       assertEquals(0,manager.getHistory().size());
    }

    @Test
    void setEpicStartTimeTask() {
        initTasks();
        task.setStartTime(LocalDateTimeHelper.convertToLocalDateTime("06.02.2023 19:30"));
        assertEquals("06.02.2023 19:30", task.getStartTime().toString());

    }

    @Test
    void setEndTimeEpic() {
        initTasks();
        epic.setEndTime(LocalDateTimeHelper.convertToLocalDateTime("06.02.2023 19:30"));
        assertEquals("06.02.2023 19:30", epic.getEndTime().toString(), "Время окончания эпика неверно");
    }

    @Test
    void convertToLocalDateTime() {
       LocalDateTime localDateTime = LocalDateTimeHelper.convertToLocalDateTime("06.02.2023 19:30");
       assertEquals("06.02.2023 19:30", localDateTime.toString(), "Неверная конвертация");
    }
}