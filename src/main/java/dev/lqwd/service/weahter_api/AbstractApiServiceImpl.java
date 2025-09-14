package dev.lqwd.service.weahter_api;

import dev.lqwd.service.ApiHttpClient;
import dev.lqwd.service.JsonSerialization;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.*;

@Slf4j
@AllArgsConstructor
public abstract class AbstractApiServiceImpl<T> implements AbstractApiService<T> {

    private final Class<T> type;
    private final JsonSerialization jsonSerialization;
    private final ApiHttpClient apiHttpClient;

    @Override
    public T findFirst(String uri){
        return Optional.of(fetchApiData(uri).get(0)).orElseThrow();
    }

    @Override
    public List<T> fetchApiData(String uri) {
        try {
            HttpResponse<String> response = apiHttpClient.executeRequest(uri);

            if (!isSuccessful(response.statusCode())) {
                handleError(response);
                return Collections.emptyList();
            }
            return jsonSerialization.serialize(response.body(), type);

        } catch (Exception e) {
            handleException(e, uri);
            return Collections.emptyList();
        }
    }

    private boolean isSuccessful(int statusCode) {
        return statusCode >= SC_OK && statusCode < SC_MULTIPLE_CHOICES;
    }

    private void handleError(HttpResponse<String> response) {
        log.warn("HTTP error {} for URI: {}", response.statusCode(), response.uri());
    }

    private void handleException(Exception e, String uri) {
        log.error("Failed to fetch data from: {}", uri, e);
    }


}
