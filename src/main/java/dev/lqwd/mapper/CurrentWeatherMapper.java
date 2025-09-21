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

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "main.temp", target = "temp", qualifiedByName = "roundToScale2")
    @Mapping(source = "sys.country", target = "country")
    @Mapping(source = "main.feelsLike", target = "feelsLike", qualifiedByName = "roundToScale2")
    @Mapping(source = "main.humidity", target = "humidity")
    CurrentWeatherResponseDTO toResponseDto(ApiCurrentWeatherResponseDTO weatherOfLocationDTO);

    @Named("roundToScale2")
    default BigDecimal round(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(1, RoundingMode.HALF_UP);
    }

    @AfterMapping
    default void mapCurrentWeatherFields(ApiCurrentWeatherResponseDTO weatherDTO,
                                         @MappingTarget CurrentWeatherResponseDTO responseDTO) {

        if (weatherDTO.getWeather() == null || weatherDTO.getWeather().isEmpty()) {
            return;
        }

        ApiCurrentWeatherResponseDTO.Weather weather = weatherDTO.getWeather().get(0);
        String description = weather.getDescription();
        String iconCode = weather.getIcon();

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
