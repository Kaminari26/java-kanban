package ru.yandex.mishalov.schedule.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ru.yandex.mishalov.schedule.KVTaskClient;
import ru.yandex.mishalov.schedule.tasks.Epic;
import ru.yandex.mishalov.schedule.tasks.Subtask;
import ru.yandex.mishalov.schedule.tasks.Task;
import ru.yandex.mishalov.schedule.tasks.TypeTask;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private final Gson gson;
    private final KVTaskClient client;
    public HttpTaskManager(int port) throws IOException, InterruptedException {
        this(port, false);
    }
    public HttpTaskManager(int port, boolean load) throws IOException, InterruptedException {
        super(null);
        gson = new GsonBuilder().create();
        client = new KVTaskClient(port);
        if (load) {
            load();
        }
    }
    protected void addTasks(List<? extends Task> tasks) {
        for (Task task : tasks) {
            final int id = task.getId();
            if (id > countId) {
                countId = id;
            }
            TypeTask type = task.getType();
            if (type == TypeTask.TASK) {
                this.tasks.put(id, task);
                prioritizedTasks.put(task.getStartTime(), task);
            } else if (type == TypeTask.SUBTASK) {
                subTasks.put(id, (Subtask) task);
                prioritizedTasks.put(task.getStartTime(), task);
            } else if (type == TypeTask.EPIC) {
                epics.put(id, (Epic) task);
            }
        }
    }
    private void load() {
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(tasks);
        ArrayList<Epic> epics = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        addTasks(epics);
        ArrayList<Subtask> subtasks = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        addTasks(subtasks);
        List<Integer> history = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Integer>>() {
        }.getType());
        for (Integer taskId : history) {
            inMemoryHistoryManager.add(findTask(taskId));
        }
    }
    @Override
    protected void save() {
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
        client.put("tasks", jsonTasks);
        String jsonSubtasks = gson.toJson(new ArrayList<>(subTasks.values()));
        client.put("subtasks", jsonSubtasks);
        String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
        client.put("epics", jsonEpics);
        String jsonHistory = gson.toJson(inMemoryHistoryManager.getHistory().stream().map(Task::getId).collect(Collectors.toList()));
        client.put("history", jsonHistory);
    }
}