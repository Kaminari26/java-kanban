package ru.yandex.mishalov.schedule;

import ru.yandex.mishalov.schedule.manager.Managers;
import ru.yandex.mishalov.schedule.manager.TaskManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class KVTaskClient {
    private String API_TOKEN;
    private String kvServerUrl;
    private final String KVSERVER_REGISTER_PATH = "/register";
    private final String KVSERVER_LOAD_PATH = "/load";
    private final String KVSERVER_SAVE_PATH = "/save";
    private HttpClient kvServerClient;
    public KVTaskClient(String kvServerUrl) throws IOException, InterruptedException {
        this.kvServerUrl = kvServerUrl;
        kvServerClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(kvServerUrl+KVSERVER_REGISTER_PATH))
                .build();
        HttpResponse<String> response = kvServerClient.send(request,  HttpResponse.BodyHandlers.ofString());
        API_TOKEN = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(kvServerUrl+KVSERVER_SAVE_PATH+"/"+key+"?API_TOKEN="+API_TOKEN))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = kvServerClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Статус:"+response.statusCode()+"\nbody:"+response.body());
    }

    public String load(String key) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(kvServerUrl+KVSERVER_LOAD_PATH+"/"+key+"?API_TOKEN="+API_TOKEN))
                .GET()
                .build();
        HttpResponse<String> response = kvServerClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
