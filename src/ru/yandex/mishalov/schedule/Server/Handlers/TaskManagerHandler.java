package ru.yandex.mishalov.schedule.Server.Handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.mishalov.schedule.Server.Endpoint;
import ru.yandex.mishalov.schedule.manager.TaskManager;
import ru.yandex.mishalov.schedule.tasks.Subtask;
import ru.yandex.mishalov.schedule.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class TaskManagerHandler implements HttpHandler {

    private static final Gson gson = new Gson();
    private static TaskManager manager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public TaskManagerHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(),
                exchange.getRequestMethod(),
                exchange.getRequestURI().getQuery());
        switch (endpoint) {
            case GET_TASKS: {
                writeResponse(exchange, gson.toJson(manager.getTaskList()), 200);
                break;
            }
            case GET_TASKS_ID: {
                handleGetTask(exchange);
                break;
            }
            case ADD_TASK: {
                handlePostNewTask(exchange);
                break;
            }
            case DELETE_TASK: {
                handleDeleteTask(exchange);
                break;
            }
            case DELETE_ALL_TASKS: {
                manager.clearAllTask();
                writeResponse(exchange, "Все таски были удалены", 200);
            }
            case GET_SUBTASKS: {
                writeResponse(exchange, gson.toJson(manager.getSubtaskList()), 200);
            }
            case GET_SUBTASKS_ID: {
                handleGetSubTask(exchange);
            }
            case ADD_SUBTASK:{
                handlePostNewSubtask(exchange);
            }
            default:
                writeResponse(exchange, "Запрос не распознан", 404);
                break;
        }
    }

    private Optional<Integer> targetId (HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        String id = query.substring(3);
        try {
            return Optional.of(Integer.parseInt(id));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }


    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
        exchange.sendResponseHeaders(responseCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
        exchange.close();
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod, String query) {
        String[] pathParts = requestPath.split("/");

        if (requestMethod.equals("GET")) {
            if (pathParts[2].equals("task") && query == null) {
                return Endpoint.GET_TASKS;
            } else if (pathParts[2].equals("task") && query != null) {
                return Endpoint.GET_TASKS_ID;
            }
            if (pathParts[2].equals("subtask") && query == null){
                return Endpoint.GET_SUBTASKS;
            }
        } else if (requestMethod.equals("POST")) {
            if (pathParts[2].equals("task")) {
                return Endpoint.ADD_TASK;
            }
        } else if (requestMethod.equals("DELETE")) {
            if (pathParts[2].equals("task") && query != null) {
                return Endpoint.DELETE_TASK;
            } else if (pathParts[2].equals("task") && query == null) {
                return Endpoint.DELETE_ALL_TASKS;
            }
        }
        return Endpoint.UNKNOWN;
    }

    private String getResponseBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
        return body;
    }
       private void handleGetTask(HttpExchange exchange) throws IOException {

        Optional<Integer> optionalInteger = targetId(exchange);
         if (optionalInteger.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор задачи", 400);
             return;
          }
         int id = optionalInteger.get();
         if (manager.getTask(id) != null) {
             writeResponse(exchange, gson.toJson(manager.getTask(id)), 200);

            return;
         }
        writeResponse(exchange, "Задача с идентификатором " + id + " не найдена", 404);
     }
    private void handlePostNewTask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        Task newTask;
        try {
            newTask = gson.fromJson(body, Task.class);
        } catch (JsonSyntaxException ex) {
            writeResponse(exchange, "Получен некорректный JSON", 400);
            return;
        }
        manager.addTask(newTask);
        writeResponse(exchange, "Задача добавлена", 200);
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        if (manager.getTask(targetId(exchange).get()) != null) {
            manager.removeTask(targetId(exchange).get());
            writeResponse(exchange, "Задача удалена успешно!", 200);
        } else {
            writeResponse(exchange, "Задача не найдена", 404);
        }
    }
    private void handleGetSubTask(HttpExchange exchange) throws IOException {

        Optional<Integer> optionalInteger = targetId(exchange);
        if (optionalInteger.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор подзадачи", 400);
            return;
        }
        int id = optionalInteger.get();
        if (manager.getSubtask(id) != null) {
            writeResponse(exchange, gson.toJson(manager.getSubtask(id)), 200);

            return;
        }
        writeResponse(exchange, "подзадача с идентификатором " + id + " не найдена", 404);
    }
   private void handlePostNewSubtask(HttpExchange exchange) throws  IOException {
       String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
       Subtask newSubtask;
       try {
           newSubtask = gson.fromJson(body, Subtask.class);
       } catch (JsonSyntaxException ex) {
           writeResponse(exchange, "Получен некорректный JSON", 400);
           return;
       }
       manager.addTask(newSubtask);
       writeResponse(exchange, "подзадача добавлена", 200);
   }
}

