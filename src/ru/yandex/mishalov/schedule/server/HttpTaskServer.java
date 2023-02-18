package ru.yandex.mishalov.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.mishalov.schedule.manager.Managers;
import ru.yandex.mishalov.schedule.server.handlers.TaskManagerHandler;
import ru.yandex.mishalov.schedule.manager.TaskManager;
import ru.yandex.mishalov.schedule.tasks.Epic;
import ru.yandex.mishalov.schedule.tasks.Subtask;
import ru.yandex.mishalov.schedule.tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;
    public HttpTaskServer() throws IOException, InterruptedException {
        this(Managers.getDefault());
    }
    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = new GsonBuilder().create();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        final HttpTaskServer server = new HttpTaskServer();
        server.start();
//        server.stop();
    }
    private void handler(HttpExchange h) {
        try (h) {
            System.out.println("\n/tasks: " + h.getRequestURI());
            final String path = h.getRequestURI().getPath().substring(7);
            switch (path) {
                case "" -> {
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/ Ждёт GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                    }
                    final String response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(h, response);
                }
                case "history" -> {
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/history ждёт GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                    }
                    final String response = gson.toJson(taskManager.getHistory());
                    sendText(h, response);
                }
                case "task" -> handleTask(h);
                case "subtask" -> handleSubtask(h);
                case "subtask/epic" -> {
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/subtask/epic ждёт GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                    }
                    final String query = h.getRequestURI().getQuery();
                    String idParam = query.substring(3);// ?id=
                    final int id = Integer.parseInt(idParam);
                    final List<Subtask> subtasks = taskManager.getEpicSubtasks(id);
                    final String response = gson.toJson(subtasks);
                    System.out.println("Получили подзадачи эпика id=" + id);
                    sendText(h, response);
                }
                case "epic" -> handleEpic(h);
                default -> {
                    System.out.println("Неизвестный зарос: " + h.getRequestURI());
                    h.sendResponseHeaders(404, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void handleTask(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET" -> {
                if (query == null) {
                    final List<Task> tasks = taskManager.getTaskList();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили все задачи");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                final Task task = taskManager.getTask(id);
                final String response = gson.toJson(task);
                System.out.println("Получили задачу id=" + id);
                sendText(h, response);
            }
            case "DELETE" -> {
                if (query == null) {
                    taskManager.clearAllTask();
                    System.out.println("Удалили все задачи");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                taskManager.removeTask(id);
                System.out.println("Удалили задачу id=" + id);
                h.sendResponseHeaders(200, 0);
            }
            case "POST" -> {
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body c задачей  пустой. указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final Task task = gson.fromJson(json, Task.class); //
                final Integer id = task.getId();
                if (id != null) {
                    taskManager.updateTask(task);
                    System.out.println("Обновили задачу id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    taskManager.addTask(task);
                    System.out.println("Создали задачу id=" + id);
                    final String response = gson.toJson(task);
                    sendText(h, response);
                }
            }
            default -> {
                System.out.println("/task получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        }
    }
    private void handleSubtask(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET" -> {
                if (query == null) {
                    final List<Subtask> tasks = taskManager.getSubtaskList();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили все подзадачи");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                final Task task = taskManager.getSubtask(id);
                final String response = gson.toJson(task);
                System.out.println("Получили подзадачу id=" + id);
                sendText(h, response);
            }
            case "DELETE" -> {
                if (query == null) {
                    taskManager.clearAllSubtask();
                    System.out.println("Удалили все подзадачи");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                taskManager.removeSubtask(id);
                System.out.println("Удалили подзадачу id=" + id);
                h.sendResponseHeaders(200, 0);
            }
            case "POST" -> {
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body c задачей  пустой. указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final Subtask task = gson.fromJson(json, Subtask.class); //
                final Integer id = task.getId();
                if (id != null) {
                    taskManager.updateSubtask(task);
                    System.out.println("Обновили подзадачу id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    taskManager.addSubTask(task);
                    System.out.println("Создали подзадачу id=" + id);
                    final String response = gson.toJson(task);
                    sendText(h, response);
                }
            }
            default -> {
                System.out.println("/subtask получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        }
    }
    private void handleEpic(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET" -> {
                if (query == null) {
                    final List<Epic> tasks = taskManager.getEpicList();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили все эпики");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                final Task task = taskManager.getEpic(id);
                final String response = gson.toJson(task);
                System.out.println("Получили эпик id=" + id);
                sendText(h, response);
            }
            case "DELETE" -> {
                if (query == null) {
                    taskManager.clearAllEpic();
                    System.out.println("Удалили все эпики");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                taskManager.removeEpic(id);
                System.out.println("Удалили эпик id=" + id);
                h.sendResponseHeaders(200, 0);
            }
            case "POST" -> {
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body c эпиком  пустой. указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final Epic task = gson.fromJson(json, Epic.class); //
                final Integer id = task.getId();
                if (id != null) {
                    taskManager.updateEpic(task);
                    System.out.println("Обновили эпик id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    taskManager.addEpic(task);
                    System.out.println("Создали эпик id=" + id);
                    final String response = gson.toJson(task);
                    sendText(h, response);
                }
            }
            default -> {
                System.out.println("/epic получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        }
    }
    public void start() {
        System.out.println("Started TaskServer " + PORT);
        System.out.println("http://localhost:" + PORT + "/tasks");
        server.start();
    }
    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }
    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
