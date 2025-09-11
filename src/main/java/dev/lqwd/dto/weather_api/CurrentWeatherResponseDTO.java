package dev.lqwd.dto.weather_api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrentWeatherResponseDTO {

    private Long id;
    private String iconUrl;
    private BigDecimal temp;
    private String name;
    private String country;
    private BigDecimal feelsLike;
    private String description;
    private int humidity;
}
