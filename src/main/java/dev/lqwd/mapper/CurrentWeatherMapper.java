package dev.lqwd.mapper;

import dev.lqwd.dto.weather.api_response.ApiCurrentWeatherResponseDTO;
import dev.lqwd.dto.weather.CurrentWeatherResponseDTO;
import dev.lqwd.uri_builder.UriApiIconBuilder;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper
public interface CurrentWeatherMapper {

    CurrentWeatherMapper INSTANCE = Mappers.getMapper(CurrentWeatherMapper.class);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "main.temp", target = "temp", qualifiedByName = "roundToInteger")
    @Mapping(source = "sys.country", target = "country")
    @Mapping(source = "main.feelsLike", target = "feelsLike", qualifiedByName = "roundToInteger")
    @Mapping(source = "main.humidity", target = "humidity")
    CurrentWeatherResponseDTO toResponseDto(ApiCurrentWeatherResponseDTO weatherOfLocationDTO);

    @Named("roundToInteger")
    default BigDecimal round(BigDecimal value){
        if(value == null){
           return null;
        }
        return value.setScale(1, RoundingMode.HALF_UP);
    }

    @AfterMapping
    default void mapCurrentWeatherFields(ApiCurrentWeatherResponseDTO weatherDTO,
                                         @MappingTarget CurrentWeatherResponseDTO responseDTO){

        if (weatherDTO.getWeather() == null || weatherDTO.getWeather().isEmpty()){
            return;
        }

        ApiCurrentWeatherResponseDTO.Weather weather = weatherDTO.getWeather().get(0);
        String description = weather.getDescription();
        String icon = weather.getIcon();

        if (description != null && !description.isBlank()){
            responseDTO.setDescription(description);
        }

        if (icon != null && !icon.isBlank()){
            responseDTO.setIconUrl(new UriApiIconBuilder(icon).build());
        }
    }
}
