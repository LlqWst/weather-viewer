package dev.lqwd.mapper;

import dev.lqwd.dto.weather.api_response.ApiCurrentWeatherResponseDTO;
import dev.lqwd.dto.weather.CurrentWeatherResponseDTO;
import dev.lqwd.utils.Validator;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring")
public interface CurrentWeatherMapper {

    String URI_ICON = "https://openweathermap.org/img/wn/";
    String SIZE_100_X_100PX = "@2x";
    String EXTENSION = ".png";
    int FIRST = 0;
    int TO_DECIMAL = 1;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "main.temp", target = "temp", qualifiedByName = "roundToScale1")
    @Mapping(source = "sys.country", target = "country")
    @Mapping(source = "main.feelsLike", target = "feelsLike", qualifiedByName = "roundToScale1")
    @Mapping(source = "main.humidity", target = "humidity")
    CurrentWeatherResponseDTO toResponseDto(ApiCurrentWeatherResponseDTO weatherOfLocationDTO);

    @Named("roundToScale1")
    default BigDecimal round(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(TO_DECIMAL, RoundingMode.HALF_UP);
    }

    @AfterMapping
    default void mapCurrentWeatherFields(ApiCurrentWeatherResponseDTO weatherDTO,
                                         @MappingTarget CurrentWeatherResponseDTO responseDTO) {

        if (weatherDTO.weather() == null || weatherDTO.weather().isEmpty()) {
            return;
        }

        ApiCurrentWeatherResponseDTO.Weather weather = weatherDTO.weather().get(FIRST);
        String description = weather.description();
        String iconCode = weather.icon();

        if (description != null && !description.isBlank()) {
            responseDTO.setDescription(description);
        }

        if (iconCode != null && !iconCode.isBlank()) {
            String urlForIcon = URI_ICON + iconCode + SIZE_100_X_100PX + EXTENSION;
            Validator.validateUrlLength(urlForIcon.length());
            responseDTO.setIconUrl(urlForIcon);
        }
    }
}
