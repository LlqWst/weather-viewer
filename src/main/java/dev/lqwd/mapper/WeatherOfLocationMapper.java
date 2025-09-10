package dev.lqwd.mapper;

import dev.lqwd.dto.weather_api.ApiCurrentWeatherDTO;
import dev.lqwd.dto.weather_api.WeatherOfLocationResponseDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper
public interface WeatherOfLocationMapper {

    WeatherOfLocationMapper INSTANCE = Mappers.getMapper(WeatherOfLocationMapper.class);
    String ICON_URL = "https://openweathermap.org/img/wn/%s@2x.png";

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "main.temp", target = "temp", qualifiedByName = "roundToInteger")
    @Mapping(source = "sys.country", target = "country")
    @Mapping(source = "main.feelsLike", target = "feelsLike", qualifiedByName = "roundToInteger")
    @Mapping(source = "main.humidity", target = "humidity")
    WeatherOfLocationResponseDTO toResponseDto(ApiCurrentWeatherDTO weatherOfLocationDTO);

    @Named("roundToInteger")
    default BigDecimal round(BigDecimal value){
        if(value == null){
           return null;
        }
        return value.setScale(0, RoundingMode.HALF_UP);
    }

    @AfterMapping
    default void mapCurrentWeatherFields(ApiCurrentWeatherDTO weatherDTO, @MappingTarget WeatherOfLocationResponseDTO responseDTO){
        if (weatherDTO.getWeather() == null || weatherDTO.getWeather().isEmpty()){
            return;
        }

        ApiCurrentWeatherDTO.Weather weather = weatherDTO.getWeather().get(0);
        String description = weather.getDescription();
        String icon = weather.getIcon();

        if (description != null && !description.isBlank()){
            responseDTO.setDescription(description);
        }

        if (icon != null && !icon.isBlank()){
            responseDTO.setIconUrl(ICON_URL.formatted(icon));
        }
    }
}
