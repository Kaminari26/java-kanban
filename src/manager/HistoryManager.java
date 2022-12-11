package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    List<Task> history = new ArrayList<>();
    public  void add(Task task);
    public List getHistory();
}
