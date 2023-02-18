package ru.yandex.mishalov.schedule;

import ru.yandex.mishalov.schedule.manager.ManagerSaveException;
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
        private final String url;
        private final String apiToken;
        public KVTaskClient(int port) {
            url = "http://localhost:" + port + "/";
            apiToken = register(url);
        }
        private String register(String url) {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url + "register"))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    throw new ManagerSaveException("Can't do save request, status code: " + response.statusCode());
                }
                return response.body();
            } catch (IOException | InterruptedException e) {
                throw new ManagerSaveException("Can't do save request", e);
            }
        }
        public String load(String key) {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    throw new ManagerSaveException("Can't do save request, status code: " + response.statusCode());
                }
                return response.body();
            } catch (IOException | InterruptedException e) {
                throw new ManagerSaveException("Can't do save request", e);
            }
        }
        public void put(String key, String value) {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                        .POST(HttpRequest.BodyPublishers.ofString(value))
                        .build();
                HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
                if (response.statusCode() != 200) {
                    throw new ManagerSaveException("Can't do save request, status code: " + response.statusCode());
                }
            } catch (IOException | InterruptedException e) {
                throw new ManagerSaveException("Can't do save request", e);
            }
        }
    }
