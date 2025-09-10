package dev.lqwd.dto.weather_api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiCurrentWeatherDTO {

    private List<Weather> weather;
    private Main main;
    private Sys sys;
    private String name;
    private String timezone;
    private String id;
    private String cod;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Weather {
        private String id;
        private String main;
        private String description;
        private String icon;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Main {
        private BigDecimal temp;

        @JsonProperty("feels_like")
        private BigDecimal feelsLike;
        private int humidity;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Sys {
        private String country;
    }
}