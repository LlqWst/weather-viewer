package dev.lqwd.service.weahter_api.infrastructure;

import dev.lqwd.exception.HttpClientException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class ApiHttpClient {

    private static final int TIMEOUT_SEC = 10;
    private final HttpClient httpClient;

    public ApiHttpClient() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SEC))
                .build();
    }

    public HttpResponse<String> executeRequest(String uri) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(TIMEOUT_SEC))
                    .GET()
                    .build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            throw new HttpClientException("Request failed: " + uri, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HttpClientException("Request interrupted: " + uri, e);
        }
    }
}
