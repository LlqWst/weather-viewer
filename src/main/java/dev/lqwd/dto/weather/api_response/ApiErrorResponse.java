package dev.lqwd.dto.weather.api_response;

import lombok.Builder;


@Builder
public record ApiErrorResponse(
        int code,
        String message) {

}