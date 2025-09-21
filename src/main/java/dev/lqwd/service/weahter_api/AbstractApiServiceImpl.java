package dev.lqwd.service.weahter_api;

import dev.lqwd.dto.weather.api_response.ApiErrorResponse;
import dev.lqwd.exception.api_weather_exception.ApiException;
import dev.lqwd.service.weahter_api.infrastructure.ApiExceptionHandler;
import dev.lqwd.service.weahter_api.infrastructure.ApiHttpClient;
import dev.lqwd.service.weahter_api.infrastructure.JsonResponseParser;
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
    private final JsonResponseParser jsonResponseParser;
    private final ApiHttpClient apiHttpClient;
    private final ApiExceptionHandler apiExceptionHandler;

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
        return jsonResponseParser.deserialize(body, type);
    }

    private void handleError(String body, int statusCode, String uri) {
        List<ApiErrorResponse> error = jsonResponseParser.deserialize(body, ApiErrorResponse.class);
        if (error.isEmpty()) {
            log.warn("Empty body for error-response from API. URI: {}, body: {}", uri, body);
            throw new ApiException("API error:" + statusCode + " URI: " + uri);
        }
        apiExceptionHandler.validate(error.get(0));
    }

    private Boolean isError(int statusCode) {
        return statusCode < SC_OK || statusCode >= SC_MULTIPLE_CHOICES;
    }

}



