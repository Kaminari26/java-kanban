package ru.yandex.mishalov.schedule.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.mishalov.schedule.manager.HistoryManager;
import ru.yandex.mishalov.schedule.manager.InMemoryHistoryManager;
import ru.yandex.mishalov.schedule.tasks.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest  {

    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
        epic = new Epic("EP1", TypeTask.EPIC, "as1111fas", TaskStatus.NEW);
        subtask = new Subtask("SUB2", TypeTask.SUBTASK, "as1111fas", TaskStatus.DONE, epic.getId());
    }

    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(1, history.size(), "История не пустая");
    }

    @Test
    void addTwice() {
        historyManager.add(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(1, history.size(), "История не пустая");
    }

    @Test
    void addDifferentTask() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(3, history.size(), "История не пустая");
        assertEquals(task, history.get(0), "Задачи в порядке добавления");
        assertEquals(epic, history.get(1), "Задачи в порядке добавления");
        assertEquals(subtask, history.get(2), "Задачи в порядке добавления");
    }

    @Test
    void remove() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(3, history.size(), "История не пустая");
        assertEquals(task, history.get(0), "Задачи в порядке добавления");
        assertEquals(epic, history.get(1), "Задачи в порядке добавления");
        assertEquals(subtask, history.get(2), "Задачи в порядке добавления");
        historyManager.remove(subtask.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(2, history.size(), "История не пустая");
        assertEquals(task, history.get(0), "Задачи в порядке добавления");
        assertEquals(epic, history.get(1), "Задачи в порядке добавления");
        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(1, history.size(), "История не пустая");
        assertEquals(epic, history.get(0), "Задачи в порядке добавления");
        historyManager.remove(epic.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertTrue(history.isEmpty(), "История пустая");
    }

    @Test
    void removeMiddle() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(3, history.size(), "История не пустая");
        assertEquals(task, history.get(0), "Задачи в порядке добавления");
        assertEquals(epic, history.get(1), "Задачи в порядке добавления");
        assertEquals(subtask, history.get(2), "Задачи в порядке добавления");
        historyManager.remove(epic.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "История не пустая");
        assertEquals(2, history.size(), "История не пустая");
        assertEquals(task, history.get(0), "Задачи в порядке добавления");
        assertEquals(subtask, history.get(1), "Задачи в порядке добавления");
    }

    @Test
    void removeNotExist() {
        historyManager.remove(task.getId());
        historyManager.remove(subtask.getId());
        historyManager.remove(epic.getId());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertTrue(history.isEmpty(), "История пустая");
    }

    @Test
    void getHistory() {
        Task task1 = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
        Task task2 = new Task("T2", TypeTask.TASK, "asfas", TaskStatus.NEW);
        task1.setId(1);
        task2.setId(2);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        List<Task> testList = new ArrayList<>();
        testList.add(task1);
        testList.add(task2);
        assertEquals(2, inMemoryHistoryManager.getHistory().size());
        assertEquals(testList, inMemoryHistoryManager.getHistory());
    }
}