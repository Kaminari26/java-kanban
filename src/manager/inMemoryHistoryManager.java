package manager;

import tasks.Task;

import java.util.List;

public class inMemoryHistoryManager implements HistoryManager {
    final static int MaxHistoryCount = 10;

    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > MaxHistoryCount) {

            history.remove(0);
        }
    }

    @Override
    public List getHistory() {
        return history;
    }
}
