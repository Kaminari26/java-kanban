package manager;

public class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}