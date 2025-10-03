package dev.lqwd.dto.weather;

import dev.lqwd.dto.weather.api_response.ApiLocationResponseDTO;
import lombok.Builder;


@Builder
public record LocationWithStatusResponseDTO(
        ApiLocationResponseDTO location,
        boolean isAdded
) {
}
