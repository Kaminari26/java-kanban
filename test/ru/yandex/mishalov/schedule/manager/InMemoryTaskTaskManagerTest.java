package ru.yandex.mishalov.schedule.manager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.opentest4j.AssertionFailedError;
import ru.yandex.mishalov.schedule.tasks.Epic;
import ru.yandex.mishalov.schedule.tasks.Subtask;
import ru.yandex.mishalov.schedule.tasks.TaskStatus;
import ru.yandex.mishalov.schedule.tasks.TypeTask;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskTaskManagerTest extends TaskManagerTest<InMemoryTaskTaskManager> {

    public InMemoryTaskTaskManagerTest() {
        super(new InMemoryTaskTaskManager());
    }

    @Test
    void nullEpicSubtaskTest() {
        Epic epic1 = new Epic("EP1", TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        manager.addEpic(epic1);
        final AssertionFailedError exception = assertThrows(AssertionFailedError.class,
                new Executable() {
                    @Override
                    public void execute(){
                        assertNull(manager.getEpic(1).getSubtaskIds());
                    }
                });
        assertEquals("expected: <null> but was: <[]>" , exception.getMessage());

    }

    @Test
    void epicNullSubtaskStatus() {
        Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        manager.addEpic(epic1);
        assertEquals(TaskStatus.NEW,manager.getEpic(1).getStatus());
    }

    @Test
    void epicAllSubtaskStatusNew() {
        Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        Subtask sub1 = new Subtask("SUB1",TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW, 1);
        Subtask sub2 = new Subtask("SUB2",TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW, 1);
        manager.addEpic(epic1);
        manager.addSubTask(sub1);
        manager.addSubTask(sub2);
        assertEquals(TaskStatus.NEW,manager.getEpic(1).getStatus());
    }

    @Test
    void epicAllSubtaskStatusDone() {
        Epic epic1 = new Epic("EP1",TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        Subtask sub1 = new Subtask("SUB1",TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, 1);
        Subtask sub2 = new Subtask("SUB2",TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, 1);
        manager.addEpic(epic1);
        manager.addSubTask(sub1);
        manager.addSubTask(sub2);
        assertEquals(TaskStatus.DONE,manager.getEpic(1).getStatus());
    }

    @Test
    void epicAllSubtaskStatusDoneOrNew() {
        Epic epic1 = new Epic("EP1", TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        Subtask sub1 = new Subtask("SUB1", TypeTask.SUBTASK, "as1111fas", TaskStatus.NEW, 1);
        Subtask sub2 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, 1);
        manager.addEpic(epic1);
        manager.addSubTask(sub1);
        manager.addSubTask(sub2);
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpic(1).getStatus());
    }

    @Test
    void epicAllSubtaskStatusInProgress() {
        Epic epic1 = new Epic("EP1", TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        Subtask sub1 = new Subtask("SUB1", TypeTask.SUBTASK, "as1111fas", TaskStatus.IN_PROGRESS, 1);
        Subtask sub2 = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.IN_PROGRESS, 1);
        manager.addEpic(epic1);
        manager.addSubTask(sub1);
        manager.addSubTask(sub2);
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpic(1).getStatus());
    }
}