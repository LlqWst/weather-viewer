package dev.lqwd.dto.weather_api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiWeatherResponseDTO {

    private List<Weather> weather;
    private Main main;
    private Sys sys;
    private String name;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Weather{
        private String main;
        private String description;
        private String icon;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Main{
        private String temp;
        private String feels_like;
        private String humidity;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Sys{
        private String country;
    }
}