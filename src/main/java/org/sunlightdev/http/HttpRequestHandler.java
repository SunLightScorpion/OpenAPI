package org.sunlightdev.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpRequestHandler {

    String url;

    public HttpRequestHandler(String url){
        this.url = url;
    }

    public void request(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            System.out.println("Answer: " + responseBody);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}