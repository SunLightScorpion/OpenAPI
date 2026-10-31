package pl.nightdev701.network;

import pl.nightdev701.logger.AbstractLogger;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class HttpRequestHandler {

    private final String url;
    private final AbstractLogger logger;
    private String body;
    private HttpRequest httpConnection;

    public HttpRequestHandler(String url, AbstractLogger logger) {
        this.url = url;
        this.logger = logger;
    }

    /**
     * Fire simple request synchronously
     */
    public void request() {
        logger.log(Level.INFO, "Send request to " + url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        this.httpConnection = request;

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            this.body = response.body();

            logger.log(Level.INFO, "Request sent");

        } catch (IOException | InterruptedException e) {
            logger.log(Level.WARNING, "Request failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Read line to line per request
     */
    public List<String> getHtmlLines() {
        try {
            URL uri = URI.create(url).toURL();
            InputStream inputStream = uri.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader.lines().toList();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Request failed: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Makes a synchronous REST API request with a specified HTTP method, headers, and optional body.
     * Note: Avoid calling this on the Minecraft main thread! Use makeApiRequestAsync instead.
     *
     * @param method      The HTTP method (e.g., GET, POST, PUT, DELETE).
     * @param headers     A map of header key-value pairs (e.g. X-API-Key).
     * @param requestBody The request body for methods like POST or PUT, or null if not needed.
     * @return The response body as a String, or null if status code != 2xx or request failed.
     */
    public String makeApiRequest(String method, Map<String, String> headers, String requestBody) {
        logger.log(Level.INFO, "Making a " + method + " request to " + url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method(method.toUpperCase(), requestBody != null && !requestBody.isEmpty()
                        ? HttpRequest.BodyPublishers.ofString(requestBody)
                        : HttpRequest.BodyPublishers.noBody());

        // Standard JSON Header setzen
        requestBuilder.header("Content-Type", "application/json");

        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }

        try {
            HttpRequest request = requestBuilder.build();
            this.httpConnection = request;

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            this.body = response.body();

            logger.log(Level.INFO, "Response received with status code " + response.statusCode());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body();
            } else {
                logger.log(Level.WARNING, "API Request returned non-2xx status: " + response.statusCode() + " | Body: " + response.body());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            logger.log(Level.WARNING, "Request failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Makes an ASYNCHRONOUS REST API request using CompletableFuture.
     * Completely non-blocking and safe for use on the Paper/Spigot main thread.
     *
     * @param method      The HTTP method (GET, POST, PUT, DELETE).
     * @param headers     A map of header key-value pairs.
     * @param requestBody The request body or null.
     * @return A CompletableFuture containing the response body string (or null on failure).
     */
    public CompletableFuture<String> makeApiRequestAsync(String method, Map<String, String> headers, String requestBody) {
        logger.log(Level.INFO, "Making async " + method + " request to " + url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method(method.toUpperCase(), requestBody != null && !requestBody.isEmpty()
                        ? HttpRequest.BodyPublishers.ofString(requestBody)
                        : HttpRequest.BodyPublishers.noBody());

        requestBuilder.header("Content-Type", "application/json");

        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }

        HttpRequest request = requestBuilder.build();
        this.httpConnection = request;

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    this.body = response.body();
                    logger.log(Level.INFO, "Async response received with status code " + response.statusCode());

                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        return response.body();
                    } else {
                        logger.log(Level.WARNING, "API Request returned non-2xx status: " + response.statusCode() + " | Body: " + response.body());
                        return null;
                    }
                })
                .exceptionally(throwable -> {
                    logger.log(Level.WARNING, "Async request failed: " + throwable.getMessage());
                    return null;
                });
    }

    /**
     * Download file from URL with custom HTTP headers (e.g., X-API-Key)
     *
     * @param output  The local file destination path.
     * @param headers A map of HTTP header key-value pairs.
     */
    public void download(String output, Map<String, String> headers) {
        logger.log(Level.INFO, "Download file to path " + output + " from " + url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET();

        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }

        try {
            HttpRequest request = requestBuilder.build();
            this.httpConnection = request;

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                try (
                        InputStream inputStream = response.body();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        FileOutputStream fileOutputStream = new FileOutputStream(output)
                ) {
                    byte[] bucket = new byte[8192];
                    int numBytesRead;

                    while ((numBytesRead = bufferedInputStream.read(bucket, 0, bucket.length)) != -1) {
                        fileOutputStream.write(bucket, 0, numBytesRead);
                    }
                }
                logger.log(Level.INFO, "Download complete!");
            } else {
                logger.log(Level.WARNING, "Download failed with HTTP status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            logger.log(Level.WARNING, "Download failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Download file from URL without headers (Overload for backward compatibility)
     *
     * @param output The local file destination path.
     */
    public void download(String output) {
        download(output, null);
    }

    /**
     * Read cached body of last request
     */
    public String getBody() {
        return body;
    }

    /**
     * Get HttpRequest connection object
     *
     * @return httpRequest
     */
    public HttpRequest getHttpConnection() {
        return httpConnection;
    }

}