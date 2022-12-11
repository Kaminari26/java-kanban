package manager;

import tasks.Task;

import java.util.List;

public class Managers <T extends Manager>  {
    public T Default;

    public T getDefault(){
       return Default;
    }
    public List<Task> getDefaultHistory(){
        return inMemoryHistoryManager.history;
    }
}
