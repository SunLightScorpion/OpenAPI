package pl.nightdev701.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpRequestHandler {

    private final String url;
    private String body;

    public HttpRequestHandler(String url) {
        this.url = url;
    }

    /** fire request */
    public void request() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            this.body = response.body();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** read body */
    public String getBody() {
        return body;
    }

}
