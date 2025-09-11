package dev.lqwd.dto.weather_api.api_response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiLocationResponseDTO {

    private String name;
    private BigDecimal lat;
    private BigDecimal lon;
    private String country;
    private String state;
}
