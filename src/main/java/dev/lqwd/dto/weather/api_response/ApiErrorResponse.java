package dev.lqwd.dto.weather.api_response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiErrorResponse {

    private int code;
    private String message;

}