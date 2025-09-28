package dev.lqwd.service.weahter_api;

import dev.lqwd.dto.weather.api_response.ApiErrorResponse;
import dev.lqwd.service.weahter_api.infrastructure.ExternalApiExceptionHandler;
import dev.lqwd.service.weahter_api.infrastructure.ApiHttpClient;
import dev.lqwd.service.weahter_api.infrastructure.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpResponse;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_MULTIPLE_CHOICES;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;


@Slf4j
@AllArgsConstructor
public abstract class AbstractApiServiceImpl<T> implements AbstractApiService<T> {

    private final Class<T> type;
    private final JsonDeserializer jsonDeserializer;
    private final ApiHttpClient apiHttpClient;
    private final ExternalApiExceptionHandler externalApiExceptionHandler;

    @Override
    public List<T> fetchApiData(String uri) {
        HttpResponse<String> response = apiHttpClient.executeRequest(uri);
        return processResponse(response, uri);
    }

    private List<T> processResponse(HttpResponse<String> response, String uri) {
        int statusCode = response.statusCode();
        String body = response.body();
        if (isError(statusCode)) {
            handleError(body, statusCode, uri);
        }
        return jsonDeserializer.deserialize(body, type);
    }

    private void handleError(String body, int statusCode, String uri) {
        log.warn("API responded with an error with and empty body. {} - {} - {}", uri, statusCode, body);
        List<ApiErrorResponse> error = jsonDeserializer.deserialize(body, ApiErrorResponse.class);
        externalApiExceptionHandler.handle(error);
    }

    private Boolean isError(int statusCode) {
        return statusCode < SC_OK || statusCode >= SC_MULTIPLE_CHOICES;
    }

}



