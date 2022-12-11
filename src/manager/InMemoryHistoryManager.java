package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    LinkedList<Task> history = new LinkedList<>(); // погуглил, ничего не понял но очень интересно)
    private final static int MaxHistoryCapacity = 10;

    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > MaxHistoryCapacity) {
            history.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
