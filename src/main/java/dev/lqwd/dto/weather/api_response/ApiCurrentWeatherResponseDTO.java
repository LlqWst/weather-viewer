package dev.lqwd.dto.weather.api_response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiCurrentWeatherResponseDTO(
        List<Weather> weather,
        Main main,
        Sys sys,
        String name,
        String timezone,
        String id,
        String cod) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Weather(
            String id,
            String main,
            String description,
            String icon) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Main(
            BigDecimal temp,

            @JsonProperty("feels_like")
            BigDecimal feelsLike,
            int humidity) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Sys(
            String country) {
    }

}