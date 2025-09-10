package dev.lqwd.service.weahter_api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Slf4j
public abstract class AbstractApiServiceImpl<T> implements AbstractApiService<T> {

    private final Class<T> type;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public AbstractApiServiceImpl(Class<T> clazz) {
        this.type = clazz;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public List<T> fetchApiData(String uri) {
        try {
            HttpResponse<String> response = executeRequest(uri);

            if (!isSuccessful(response)) {
                handleError(response);
                return Collections.emptyList();
            }
            return parseResponse(response.body());

        } catch (Exception e) {
            handleException(e, uri);
            return Collections.emptyList();
        }
    }

    private HttpResponse<String> executeRequest(String uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private List<T> parseResponse(String jsonResponse) throws JsonProcessingException {
        if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
            return Collections.emptyList();
        }

        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        if (rootNode.isArray()) {
            return parseArray(jsonResponse);
        } else if (rootNode.isObject()) {
            return parseObject(jsonResponse);
        } else {
            throw new JsonParseException("Unsupported JSON format");
        }
    }

    private List<T> parseArray(String json) throws JsonProcessingException {
        return objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, type));
    }

    private List<T> parseObject(String json) throws JsonProcessingException {
        T singleObject = objectMapper.readValue(json, type);
        return List.of(singleObject);
    }

    private boolean isSuccessful(HttpResponse<String> response) {
        return response.statusCode() >= 200 && response.statusCode() < 300;
    }

    private void handleError(HttpResponse<String> response) {
        log.warn("HTTP error {} for URI: {}", response.statusCode(), response.uri());
    }

    private void handleException(Exception e, String uri) {
        log.error("Failed to fetch data from: {}", uri, e);
    }


}
