package ru.yandex.mishalov.schedule.manager;

import java.io.File;
import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078");

    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}