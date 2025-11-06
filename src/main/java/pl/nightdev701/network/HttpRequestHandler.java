package pl.nightdev701.network;

import pl.nightdev701.logger.AbstractLogger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
     * fire request
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
            e.printStackTrace();
            logger.log(Level.WARNING, "Request failed: " + e.getMessage());
        }
    }

    /**
     * Read line to line per request
     */
    public List<String> getHtmlLines() {
        try {
            URL uri = new URL(url);
            InputStream inputStream = uri.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader.lines().toList();
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Request failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Makes a REST API request with a specified HTTP method, headers, and optional body.
     *
     * @param method      The HTTP method (e.g., GET, POST, PUT, DELETE).
     * @param headers     A map of header key-value pairs.
     * @param requestBody The request body for methods like POST or PUT, or null if not needed.
     * @return The response body as a String.
     */
    public String makeApiRequest(String method, Map<String, String> headers, String requestBody) {
        logger.log(Level.INFO, "Making a " + method + " request to " + url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method(method.toUpperCase(), requestBody != null
                        ? HttpRequest.BodyPublishers.ofString(requestBody)
                        : HttpRequest.BodyPublishers.noBody());

        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }

        try {
            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            logger.log(Level.INFO, "Response received with status code " + response.statusCode());
            return response.body();
        } catch (IOException | InterruptedException e) {
            logger.log(Level.WARNING, "Request failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Download file
     */
    public void download(String output) {

        String link = url;

        logger.log(Level.INFO, "Download file to path " + output + " from " + url);

        URL url;
        try {
            url = new URL(link);
            try (
                    InputStream inputStream = url.openStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    FileOutputStream fileOutputStream = new FileOutputStream(output)
            ) {
                byte[] bucket = new byte[2048];
                int numBytesRead;

                while ((numBytesRead = bufferedInputStream.read(bucket, 0, bucket.length)) != -1) {
                    fileOutputStream.write(bucket, 0, numBytesRead);
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "Download failed: " + e.getMessage());
            }
            logger.log(Level.INFO, "Download complete!");
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Download failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * read body
     */
    public String getBody() {
        return body;
    }

    /**
     * Get http connection
     * @return httpRequest
     */
    public HttpRequest getHttpConnection() {
        return httpConnection;
    }

}
