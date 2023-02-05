package manager;

import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;
import tasks.TypeTask;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest  {

    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    @Test
    void add() {
        Task task1 = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
        Task task2 = new Task("T2", TypeTask.TASK, "a22s", TaskStatus.NEW);
        task1.setId(1);
        task2.setId(2);
        inMemoryHistoryManager.add(task1);
        assertEquals(1, inMemoryHistoryManager.getHistory().size());
        inMemoryHistoryManager.add(task2);
        assertEquals(2,inMemoryHistoryManager.getHistory().size());

    }

    @Test
    void remove() {
        Task task1 = new Task("T1", TypeTask.TASK, "asfas", TaskStatus.NEW);
        inMemoryHistoryManager.add(task1);
        assertEquals(1, inMemoryHistoryManager.getHistory().size());
        inMemoryHistoryManager.remove(0);
        assertEquals(0, inMemoryHistoryManager.getHistory().size());

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