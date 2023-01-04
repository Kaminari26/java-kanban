package manager;

public class Managers {
    public static TaskManager getDefault(){ // так до конца и не понял, это вроде бы на что то похоже
        return new InMemoryTaskTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}