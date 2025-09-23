package dev.lqwd.service.weahter_api.infrastructure;

import dev.lqwd.exception.HttpClientException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
@AllArgsConstructor
public class ApiHttpClient {

    private static final int TIMEOUT_SEC = 10;
    public static final String REQUEST_FAILED = "Request failed: ";
    public static final String REQUEST_INTERRUPTED = "Request interrupted: ";
    private final HttpClient httpClient;

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
            throw new HttpClientException(REQUEST_FAILED + uri, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HttpClientException(REQUEST_INTERRUPTED + uri, e);
        }
    }
}
