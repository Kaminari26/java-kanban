package ru.yandex.mishalov.schedule.Server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.mishalov.schedule.Server.Handlers.TaskManagerHandler;
import ru.yandex.mishalov.schedule.manager.Managers;
import ru.yandex.mishalov.schedule.manager.TaskManager;
import ru.yandex.mishalov.schedule.tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    static TaskManager manager;
    private static final int port = 8080;
    HttpServer httpServer;
    public HttpTaskServer(TaskManager manager)
    {
        this.manager = manager;
    }

    public void StartServer()
    {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(port),0);
            httpServer.createContext("/tasks", new TaskManagerHandler(manager));
            httpServer.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void StopServer()
    {
        try {
            httpServer.stop(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
