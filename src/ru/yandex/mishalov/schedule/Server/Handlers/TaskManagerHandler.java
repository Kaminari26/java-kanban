package ru.yandex.mishalov.schedule.Server.Handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.mishalov.schedule.Server.Endpoint;
import ru.yandex.mishalov.schedule.manager.TaskManager;
import ru.yandex.mishalov.schedule.tasks.Epic;
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
                break;
            }
            case GET_SUBTASKS: {
                writeResponse(exchange, gson.toJson(manager.getSubtaskList()), 200);
                break;
            }
            case GET_SUBTASKS_ID: {
                handleGetSubTask(exchange);
                break;
            }
            case ADD_SUBTASK: {
                handlePostNewSubtask(exchange);
                break;
            }
            case DELETE_SUBTASK: {
                handleDeleteSubTask(exchange);
                break;
            }
            case DELETE_ALL_SUBTASKS: {
                manager.clearAllSubtask();
                writeResponse(exchange, "Все сабтаски были удалены", 200);
                break;
            }
            case UPDATE_TASK: {
                handleUpdateTask(exchange);
                break;
            }
            case UPDATE_SUBTASK: {
                handlePostUpdateSubtask(exchange);
                break;
            }
            case GET_EPICS: {
                writeResponse(exchange, gson.toJson(manager.getEpicList()), 200);
                break;
            }
            case GET_EPIC_ID: {
                handleGetEpic(exchange);
                break;
            }
            case GET_PRIORITIZED_TASKS: {
                handleGetPrioritizedTasks(exchange);
                break;
            }
            case ADD_EPIC: {
                handlePostNewEpic(exchange);
                break;
            }
            case UPDATE_EPIC: {
                handleUpdateEpic(exchange);
                break;
            }
            case DELETE_EPIC: {
                handleDeleteEpic(exchange);
                break;
            }
            case DELETE_ALL_EPICS: {
                manager.clearAllEpic();
                writeResponse(exchange, "Все Эпики были удалены", 200);
                break;
            }
            case GET_SUBTASKS_EPIC: {
                handleGetEpicSubtasks(exchange);
                break;
            }
            case GET_HISTORY: {
                writeResponse(exchange, gson.toJson(manager.getHistory()), 200);
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

        switch (requestMethod) {
            case "GET":
                if(pathParts.length == 2 && query == null) {
                    return Endpoint.GET_PRIORITIZED_TASKS;
                }
                switch (pathParts[2]) {
                    case "task": {
                        if (pathParts.length == 3 && query == null) {
                            return Endpoint.GET_TASKS;
                        }
                        return Endpoint.GET_TASKS_ID;
                    }

                    case "subtask": {
                        if (pathParts.length == 3 && query == null) {
                            return Endpoint.GET_SUBTASKS;
                        }
                        else if (pathParts[3].equals("epic")) {
                            return Endpoint.GET_SUBTASKS_EPIC;
                        }
                        return Endpoint.GET_SUBTASKS_ID;
                    }
                    case "epic": {
                        if (pathParts.length == 3 && query == null) {
                            return Endpoint.GET_EPICS;
                        }
                        return Endpoint.GET_EPIC_ID;
                    }
                    case "history": {
                        return Endpoint.GET_HISTORY;
                    }
                }
            case "POST":
                switch (pathParts[2]) {
                    case "task":
                        if (pathParts.length == 3 && query == null) {
                        return Endpoint.ADD_TASK;
                    }
                        return Endpoint.UPDATE_TASK;

                    case "subtask":
                        if (pathParts.length == 3 && query == null) {
                            return Endpoint.ADD_SUBTASK;
                        }
                        return Endpoint.UPDATE_SUBTASK;

                    case "epic":
                        if (pathParts.length == 3 && query == null) {
                            return Endpoint.ADD_EPIC;
                        }
                        return Endpoint.UPDATE_EPIC;
                }
                case "DELETE":
                    switch (pathParts[2]) {
                        case "task":
                            if (pathParts.length == 3 && query == null) {
                            return Endpoint.DELETE_ALL_TASKS;
                }
                            return Endpoint.DELETE_TASK;
                        case "subtask":
                            if (pathParts.length == 3 && query == null) {
                                return Endpoint.DELETE_ALL_SUBTASKS;
                            }
                            return Endpoint.DELETE_SUBTASK;
                        case "epic":
                            if (pathParts.length == 3 && query == null) {
                                return Endpoint.DELETE_ALL_EPICS;
                            }
                            return Endpoint.DELETE_EPIC;
                    }
        }

        return Endpoint.UNKNOWN;
    }

    private String getResponseBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
        return body;
    }

    private  void handleGetPrioritizedTasks(HttpExchange exchange) throws IOException {
        writeResponse(exchange,gson.toJson(manager.getPrioritizedTasks()), 200);
    }

    private  void handleGetEpic(HttpExchange exchange) throws IOException {
        Optional<Integer> optionalInteger = targetId(exchange);
        if (optionalInteger.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор задачи", 400);
            return;
        }
        int id = optionalInteger.get();
        if (manager.getEpic(id) != null) {
            writeResponse(exchange, gson.toJson(manager.getEpic(id)), 200);
            return;
        }
        writeResponse(exchange, "Задача с идентификатором " + id + " не найдена", 404);
    }

    private void handleGetEpicSubtasks(HttpExchange exchange) throws IOException {
        Optional<Integer> optionalInteger = targetId(exchange);
        if (optionalInteger.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор эпика", 400);
            return;
        }
        int id = optionalInteger.get();
        if (manager.getEpic(id) != null) {
            if (manager.getSubTaskByEpicId(id) != null) {
                writeResponse(exchange, gson.toJson(manager.getSubTaskByEpicId(id)), 200);
                return;
            }
            writeResponse(exchange, "У эпика с идентификатором " + id + " нет подзадач.", 404);
            return;
        }
        writeResponse(exchange, "Эпик с идентификатором " + id + " не найден, нельзя вывести подзадачи", 404);
    }

    private void handlePostNewEpic(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        Epic newEpic;
        try {
            newEpic = gson.fromJson(body, Epic.class);
        } catch (JsonSyntaxException ex) {
            writeResponse(exchange, "Получен некорректный JSON", 400);
            return;
        }
        manager.addEpic(newEpic);
        writeResponse(exchange, "Задача добавлена", 200);
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        Optional<Integer> optionalInteger = targetId(exchange);
        if (optionalInteger.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор задачи", 400);
            return;
        }
        int id = optionalInteger.get();
        if (manager.getEpic(id) != null) {
            manager.removeEpic(id);
            writeResponse(exchange, "Эпик успешно удален", 200);
        }else {
            writeResponse(exchange, "Задача с идентификатором " + id + " не найдена", 404);
        }
    }

    private void handleUpdateEpic(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        Epic newEpic;
        try {
            newEpic = gson.fromJson(body, Epic.class);
        } catch (JsonSyntaxException ex) {
            writeResponse(exchange, "Получен некорректный JSON", 400);
            return;
        }
        Optional<Integer> optionalInteger = targetId(exchange);
        if (optionalInteger.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор задачи", 400);
            return;
        }
        int id = optionalInteger.get();
        if (manager.getEpic(id) != null) {
            manager.updateEpic(newEpic);
            writeResponse(exchange, "Задача " + id + " обновлена", 200);
            return;
        }
        writeResponse(exchange, "Задача с идентификатором " + id + " не найдена", 404);
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
            writeResponse(exchange, gson.toJson(manager.getSubtask(id).toString()), 200);
            return;
        }
        writeResponse(exchange, "Подзадача с идентификатором " + id + " не найдена", 404);
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
       if (manager.getEpic(newSubtask.getEpicId()) == null){
           writeResponse(exchange, "Некорректный id эпика", 404);
       }
       manager.addSubTask(newSubtask);
       writeResponse(exchange, "подзадача добавлена", 200);
   }

   private void handleDeleteSubTask(HttpExchange exchange) throws IOException {
       if (manager.getSubtask(targetId(exchange).get()) != null) {
           manager.removeSubtask(targetId(exchange).get());
           writeResponse(exchange, "Задача удалена успешно!", 200);
       } else {
           writeResponse(exchange, "Задача не найдена", 404);
       }
   }

    private void handleUpdateTask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        Task newTask;
        try {
            newTask = gson.fromJson(body, Task.class);
        } catch (JsonSyntaxException ex) {
            writeResponse(exchange, "Получен некорректный JSON", 400);
            return;
        }
        Optional<Integer> optionalInteger = targetId(exchange);
        if (optionalInteger.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор задачи", 400);
            return;
        }
        int id = optionalInteger.get();
        if (manager.getTask(id) != null) {
           manager.updateTask(newTask);
                writeResponse(exchange, "Задача " + id + " обновлена", 200);
                return;

        }
        writeResponse(exchange, "Задача с идентификатором " + id + " не найдена", 404);
    }

    private void handlePostUpdateSubtask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        Subtask newSubtask;
        try {
            newSubtask = gson.fromJson(body, Subtask.class);
        } catch (JsonSyntaxException ex) {
            writeResponse(exchange, "Получен некорректный JSON", 400);
            return;
        }
        Optional<Integer> optionalInteger = targetId(exchange);
        if (optionalInteger.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор подзадачи", 400);
            return;
        }
        int id = optionalInteger.get();
        if (manager.getSubtask(id) != null) {
            manager.updateSubtask(newSubtask);
                writeResponse(exchange, "Подзадача " + id + " обновлена", 200);
        }
        writeResponse(exchange, "Подзадача с идентификатором " + id + " не найдена", 404);
    }

}


