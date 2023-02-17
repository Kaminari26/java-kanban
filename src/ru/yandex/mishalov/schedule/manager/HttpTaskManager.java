package ru.yandex.mishalov.schedule.manager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ru.yandex.mishalov.schedule.KVTaskClient;
import ru.yandex.mishalov.schedule.tasks.Task;
import ru.yandex.mishalov.schedule.tasks.TypeTask;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashMap;

public class HttpTaskManager extends FileBackedTasksManager {
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    Gson gson;
    KVTaskClient kvTaskClient;

    public HttpTaskManager(File file) {
        super(file);
    }

    public HttpTaskManager(String kvServerURI) throws IOException, InterruptedException {
        super(null);
        kvTaskClient = new KVTaskClient(kvServerURI);
        gson = new Gson();
    }


    protected void save() {
        kvTaskClient.put(key, gson.toJson(this));


    }
}
