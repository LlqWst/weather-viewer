package dev.lqwd.dto.weather.api_response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiLocationResponseDTO(
        String name,
        BigDecimal lat,
        BigDecimal lon,
        String country,
        String state) {
}
